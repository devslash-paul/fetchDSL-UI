package net.devslash.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun urlField(value: String, valid: Boolean, onValueChange: (String) -> Unit) {
  val style = if (valid) {
    MaterialTheme.typography.body1
  } else {
    MaterialTheme.typography.body1.copy(Color.Red)
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