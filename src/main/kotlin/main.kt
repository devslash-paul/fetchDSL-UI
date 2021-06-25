import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.devslash.packer.JarLibraryLoader
import net.devslash.packer.LoadedClass
import java.net.URI

class HttpViewModel(clazzes: Collection<LoadedClass>) {
  val state = mutableStateListOf(*clazzes.toTypedArray())

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

@Composable
fun urlField(value: String, valid: Boolean, onValueChange: (String) -> Unit) {
  val style = if (valid) {
    typography.body1
  } else {
    typography.body1.copy(Color.Red)
  }

  Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
    Column(
      modifier = Modifier.fillMaxHeight(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(text = "URL", modifier = Modifier.padding(3.dp))
    }
    TextField(
      value,
      onValueChange,
      textStyle = style,
      modifier = Modifier.fillMaxWidth().padding(4.dp)
    )
  }
}

@Composable
fun httpScaffold(vm: HttpViewModel) {
  var expanded by remember { mutableStateOf(true) }
  var selectedText by remember { mutableStateOf("") }

  return Column(
    modifier = Modifier.fillMaxWidth(),
  ) {
    urlField(vm.url, vm.valid) { vm.onChangeUrl(it) }
    OutlinedTextField(
      value = selectedText,
      onValueChange = { selectedText = it },
      modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
      label = { Text("Label") },
      trailingIcon = {
        Icon(Icons.Filled.ArrowDropDown, "contentDescription", Modifier.clickable { expanded = !expanded })
      }
    )
    DropdownMenu(expanded, onDismissRequest = { !expanded }, modifier = Modifier.fillMaxWidth().height(240.dp)) {
      vm.getStartingWith(selectedText).forEach {
        DropdownMenuItem({
          selectedText = it.className
          expanded = false
        }) {
          Text(it.packageName, fontSize = 8.0.sp, modifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp))
          Text(it.className, fontSize = 12.0.sp, modifier = Modifier.clickable {
          })
        }
      }
    }
  }
}

fun main() = Window {
  val elements = JarLibraryLoader().load()
  val vm = HttpViewModel(elements)
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