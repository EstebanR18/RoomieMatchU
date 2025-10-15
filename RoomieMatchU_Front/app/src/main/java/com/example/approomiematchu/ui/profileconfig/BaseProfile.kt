package com.example.approomiematchu.ui.profileconfig

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.approomiematchu.R

@Composable
fun CuestionarioBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo_profile),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.15f))
        )
        content()
    }
}

@Composable
fun ProgressDots(total: Int = 6, current: Int) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .size(12.dp)
                    .background(
                        if (index < current) colors.secondary else colors.surface,
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }
}

@Composable
fun WhiteTextField(placeholder: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(placeholder) },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun WhiteOutlinedButton(text: String, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = {},
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text, textAlign = TextAlign.Center)
    }
}

@Composable
fun QuestionWithIcon(icon: ImageVector, text: String) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(icon, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = typography.headlineMedium, color = colors.onBackground)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun YesNoButtons(onYes: () -> Unit = {}, onNo: () -> Unit = {}) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        WhiteOutlinedButton(text = "Sí", modifier = Modifier.weight(1f))
        WhiteOutlinedButton(text = "No", modifier = Modifier.weight(1f))
    }
    Spacer(modifier = Modifier.height(12.dp))
}
