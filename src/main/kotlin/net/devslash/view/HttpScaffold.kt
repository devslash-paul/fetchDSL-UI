package net.devslash.view

import CallBuilderModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun httpScaffold(vm: CallBuilderModel) {
  var expanded by remember { mutableStateOf(true) }
  var selectedText: String by remember { mutableStateOf("") }

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