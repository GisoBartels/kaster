package app.passwordkaster.android

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import app.passwordkaster.common.domainentry.DomainEntry
import app.passwordkaster.common.domainentry.DomainEntryPersistence
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.InputStream
import java.io.OutputStream

class DomainEntryPersistenceAndroid(context: Context, coroutineScope: CoroutineScope) : DomainEntryPersistence {

    private val dataStore = context.domainEntryDataStore

    override val entries = MutableStateFlow<PersistentSet<DomainEntry>>(persistentSetOf())

    init {
        coroutineScope.launch {
            entries.value = dataStore.data.first().toPersistentSet()
            entries
                .map { it.toSet() }
                .collectLatest { newValue -> dataStore.updateData { newValue } }
        }
    }
}

private val Context.domainEntryDataStore: DataStore<Set<DomainEntry>> by dataStore(
    fileName = "domain_entries.db",
    serializer = DomainEntrySerializer
)

@OptIn(ExperimentalSerializationApi::class)
private object DomainEntrySerializer : Serializer<Set<DomainEntry>> {
    override val defaultValue: Set<DomainEntry> = emptySet()

    override suspend fun readFrom(input: InputStream): Set<DomainEntry> =
        Json.decodeFromStream(input)

    override suspend fun writeTo(t: Set<DomainEntry>, output: OutputStream) {
        Json.encodeToStream(t, output)
    }
}
