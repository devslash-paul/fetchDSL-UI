import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.material.*
import net.devslash.packer.JarLibraryLoader
import net.devslash.view.httpScaffold

fun main() = Window {
  val elements = JarLibraryLoader().load()
  val vm = CallBuilderModel(elements)
  MaterialTheme {
    DesktopTheme {
      Scaffold(
        content = {
          httpScaffold(vm)
        },
        floatingActionButton = {
          Text("HI")
        }
      )
    }
  }
}