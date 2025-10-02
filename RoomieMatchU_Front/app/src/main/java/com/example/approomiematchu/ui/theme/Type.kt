package com.example.approomiematchu.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.approomiematchu.R

// Familia Nunito con las 5 variantes (asegúrate de tener estos archivos en res/font/)
val Nunito = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_semi_bold, FontWeight.SemiBold),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_extra_bold, FontWeight.ExtraBold),
    Font(R.font.nunito_black, FontWeight.Black)
)

// Tu objeto con nombres claros (titulo1, titulo2, ...)
object AppTypography {

    // Black (título principal)
    val titulo1 = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Black,
        fontSize = 28.sp
    )

    // ExtraBold (subtítulo destacado)
    val titulo2 = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 21.sp
    )

    // Bold (subtítulo normal)
    val subtitulo = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    )

    // SemiBold (textos importantes / labels)
    val normalText = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )

    // Regular (cuerpo)
    val body = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
}

// Adapter: Typography de Material que mapea a tus estilos.
// Pásalo a MaterialTheme (evita el error de tipo).
val AppMaterialTypography = Typography(
    displayLarge = AppTypography.titulo1,
    headlineLarge = AppTypography.titulo2,
    headlineMedium = AppTypography.subtitulo,
    bodyLarge = AppTypography.normalText,
    labelLarge = AppTypography.body
)
