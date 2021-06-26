import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import net.devslash.packer.LoadedClass
import java.net.URI

class CallBuilderModel(clazzes: Collection<LoadedClass>) {
  private val state = mutableStateListOf(*clazzes.toTypedArray())

  var url by mutableStateOf("")
    private set
  var valid by mutableStateOf(true)
    private set

  fun getStartingWith(st: String): List<LoadedClass> =
    state.asSequence().filter { it.className.contains(st) }.take(10).toList()

  fun onChangeUrl(url: String) {
    this.url = url
    valid = try {
      val x = URI(url)
      x.path
      true
    } catch (e: Exception) {
      false
    }
  }
}