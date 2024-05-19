package com.ardakazanci.lerptips


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ardakazanci.lerptips.ui.theme.LerpTipsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LerpTipsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimationLerp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimationLerp(modifier: Modifier = Modifier) {
    val hobbies = listOf(
        "Reading",
        "Coding",
        "Traveling",
        "Gaming",
        "Cooking",
        "Swimming",
        "Photography",
        "Writing",
        "Music",
        "Art"
    )
    val animatableList = remember { List(hobbies.size) { Animatable(300f) } }
    val colorAnimatableList = remember { List(hobbies.size) { Animatable(0f) } }
    val scaleAnimatableList = remember { List(hobbies.size) { Animatable(1f) } }
    val rotationAnimatableList = remember { List(hobbies.size) { Animatable(0f) } }
    val clickedIndex = remember { mutableStateOf<Int?>(null) }

    animatableList.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            delay(index.toLong() * 500)
            launch {
                animatable.animateTo(0f, animationSpec = tween(1500))
            }
            launch {
                colorAnimatableList[index].animateTo(1f, animationSpec = tween(1500))
            }
        }

        LaunchedEffect(clickedIndex.value) {
            if (clickedIndex.value == index) {
                launch {
                    animatable.animateTo(600f, animationSpec = tween(1500))
                }
                launch {
                    colorAnimatableList[index].animateTo(0f, animationSpec = tween(1500))
                }
                launch {
                    scaleAnimatableList[index].animateTo(1.5f, animationSpec = tween(1500))
                }
                launch {
                    rotationAnimatableList[index].animateTo(360f, animationSpec = tween(1500))
                }
            } else {
                launch {
                    animatable.animateTo(0f, animationSpec = tween(1500))
                }
                launch {
                    colorAnimatableList[index].animateTo(1f, animationSpec = tween(1500))
                }
                launch {
                    scaleAnimatableList[index].animateTo(1f, animationSpec = tween(1500))
                }
                launch {
                    rotationAnimatableList[index].animateTo(0f, animationSpec = tween(1500))
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        hobbies.forEachIndexed { index, hobby ->
            val color = Color.Lerp(Color.Gray, Color.Blue, colorAnimatableList[index].value)
            Text(
                text = hobby,
                color = color,
                modifier = Modifier
                    .offset { IntOffset(animatableList[index].value.roundToInt(), 0) }
                    .fillMaxWidth()
                    .padding(8.dp)
                    .graphicsLayer(
                        scaleX = scaleAnimatableList[index].value,
                        scaleY = scaleAnimatableList[index].value,
                        rotationZ = rotationAnimatableList[index].value
                    )
                    .noRippleClickable {
                        clickedIndex.value = index
                    }
            )
        }
    }
}

private fun Color.Companion.Lerp(startColor: Color, endColor: Color, fraction: Float): Color {
    return Color(
        red = lerp(startColor.red, endColor.red, fraction),
        green = lerp(startColor.green, endColor.green, fraction),
        blue = lerp(startColor.blue, endColor.blue, fraction),
        alpha = lerp(startColor.alpha, endColor.alpha, fraction)
    )
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}