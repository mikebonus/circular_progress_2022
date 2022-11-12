package com.example.circularprogressbars.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.circularprogressbars.ui.theme.Teal100
import com.example.circularprogressbars.ui.theme.Teal200
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max

/**
 *
 *  #1) SPEED of the rotation
 *  - can be controlled by 'private const val RotationDuration'
 *
    #2) LENGTH of the strokes
    - can be controlled by 'val adjustSweep' for 'STATIC(ie.CONSTANT-SPEED)' ROTATION
    - can be controlled by 'private const val JumpRotationAngle' for 'DYNAMIC ROTATION
    (ie. the one where stroke-length grows/shrinks as it rotates)' where
    (BaseRotationAngle + JumpRotationAngle should equal to 360)

    #3) NUMBER of the strokes
    - refer to 'MoreStrokesActivity.kt' + run it for demo.

    #4) THICKNESS of the strokes
    - can be controlled by 'strokeWidth'

    #5) SIZE/SHAPE of the progress bar (OVAL, CIRCLE, etc)
    - can be controlled by 'Modifier.size && Modifier.scale'

    #6) DYNAMIC/(CONSTANT) COLOR-TRANSITIONING EFFECTS of the strokes
    - can be controlled by 'val color by transition.animateColor'

    #7) DELAY of the rotation.
    - can be controlled by 'delayMillis' under 'tween'

    #8) DURATION of the rotation.
    - can be controlled by 'durationMillis' under 'tween'

    #9) EASING-EFFECTS (ACCELERATION/DECELERATION) of the rotation
    - (For 'STATIC'-Rotation) can be controlled by applying 'CIRCULAR-easing' for CurrentRotationOne, CurrentRotationTwo,
    baseRotationOne, baseRotationTwo, endAngleOne, endingAngleTow, startingAngleOne, startingAngleTwo

    - (For 'DYNAMIC'-Rotation) can be controlled by applying various integers to 'Easing' for CurrentRotationOne,
    CurrentRotationTwo, baseRotationOne, baseRotationTwo, endAngleOne, endingAngleTow, startingAngleOne, startingAngleTwo

    #10) YOU-CAN-ADD/DELETE/MODIFY-MORE-ON-TOP-OF-THIS-AS-YOU-WISH-EFFECTS.
    - You can come up with various combinations of all of the above effects OR bring in some more if needed.

 */


// CircularProgressIndicator Material specs
// Diameter of the indicator circle
private val CircularIndicatorDiameter = 40.dp

// SPEED OF ROTATION!
// (3000 seems to be good for average)
// (The higher the value, the slower the rotation, and vice versa)
private const val RotationDuration = 3000

// 'StartAngleOffsetOne' and 'StartAngleOffsetTwo' must be the OPPOSITE so that there are
// two strokes running exactly on the opposite sides.
private const val StartAngleOffsetOne = -90f
private const val StartAngleOffsetTwo = 90f

// How far the base point moves around the circle (270f seems to be ideal)
// (The higher the value the faster the speed becomes, and vice versa)
private const val BaseRotationAngle = 270f

// (BaseRotationAngle + JumpRotationAngle ideally should be 360f)
// (90f would be ideal for 'CONSTANT' rotation stroke-length)
private const val JumpRotationAngle = 90f

// Each rotation we want to offset the start position by this much, so we continue where
// the previous rotation ended. This is the maximum angle covered during one rotation.
private const val RotationAngleOffset = (BaseRotationAngle + JumpRotationAngle) % 360f

// The head animates for the first half of a rotation, then is static for the second half
// The tail is static for the first half and then animates for the second half
private const val HeadAndTailAnimationDuration = (RotationDuration * 0.5).toInt()
private const val HeadAndTailDelayDuration = HeadAndTailAnimationDuration

// The easing for the head and tail jump
private val CircularEasing = CubicBezierEasing(0f, 0f, 0f, 0f)

@Composable
fun DynamicCircularProgressBar() {
    val transition = rememberInfiniteTransition()
    val color by transition.animateColor(
        initialValue = Teal100,
        targetValue = Teal200,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                delayMillis = 1000
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    DynamicCircularProgressIndicator(
        modifier = Modifier
            .size(400.dp)
            .scale(0.7f, 0.7f),
        strokeWidth = 20.dp,
        color = color
    )
}


@Composable
fun DynamicCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) {

    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }
    val transition = rememberInfiniteTransition()

    // CURRENT ROTATION AROUND THE CIRCLE, SO WE KNOW WHERE TO START THE ROTATION FROM..
    val currentRotationOne by transition.animateValue(
        0,
        RotationDuration,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = RotationDuration,
                easing = CircularEasing
            )
        )
    )
    val currentRotationTwo by transition.animateValue(
        0,
        RotationDuration,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = RotationDuration,
                easing = CircularEasing
            )
        )
    )


    // How far forward (degrees) the base point should be from the start point
    val baseRotationOne by transition.animateFloat(
        0f,
        BaseRotationAngle,
        infiniteRepeatable(
            animation = tween(
                durationMillis = RotationDuration,
                easing = CircularEasing
            )
        )
    )
    val baseRotationTwo by transition.animateFloat(
        0f,
        BaseRotationAngle,
        infiniteRepeatable(
            animation = tween(
                durationMillis = RotationDuration,
                easing = CircularEasing
            )
        )
    )


    // How far forward (degrees) both the head and tail should be from the base point
    val endAngleOne by transition.animateFloat(
        0f,
        JumpRotationAngle,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at 0 with CircularEasing
                JumpRotationAngle at HeadAndTailAnimationDuration
            }
        )
    )
    val endAngleTwo by transition.animateFloat(
        0f,
        JumpRotationAngle,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at 0 with CircularEasing
                JumpRotationAngle at HeadAndTailAnimationDuration
            }
        )
    )


    // start-angle
    val startAngleOne by transition.animateFloat(
        0f,
        JumpRotationAngle,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at HeadAndTailDelayDuration with CircularEasing
                JumpRotationAngle at durationMillis
            }
        )
    )
    val startAngleTwo by transition.animateFloat(
        0f,
        JumpRotationAngle,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at HeadAndTailDelayDuration with CircularEasing
                JumpRotationAngle at durationMillis
            }
        )
    )

    Canvas(
        modifier
            .progressSemantics()
            .size(CircularIndicatorDiameter)
    ) {

        val currentRotationAngleOffsetOne = (currentRotationOne * RotationAngleOffset) % 360f
        val sweepOne = abs(endAngleOne - startAngleOne)
        val offsetOne = StartAngleOffsetOne + currentRotationAngleOffsetOne + baseRotationOne
        drawIndeterminateCircularIndicator(
            startAngleOne + offsetOne,
            strokeWidth,
            sweepOne,
            color,
            stroke
        )

        val currentRotationAngleOffsetTwo = (currentRotationTwo * RotationAngleOffset) % 360f
        val sweepTwo = abs(endAngleTwo - startAngleTwo)
        val offsetTwo = StartAngleOffsetTwo + currentRotationAngleOffsetTwo + baseRotationTwo
        drawIndeterminateCircularIndicator(
            startAngleTwo + offsetTwo,
            strokeWidth,
            sweepTwo,
            color,
            stroke
        )
    }
}

private fun DrawScope.drawIndeterminateCircularIndicator(
    startAngle: Float,
    strokeWidth: Dp,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // Length of arc is angle * radius
    // Angle (radians) is length / radius
    // The length should be the same as the stroke width for calculating the min angle
    val squareStrokeCapOffset =
        (180.0 / PI).toFloat() * (strokeWidth / (CircularIndicatorDiameter / 2)) / 2f

    // Adding a square stroke cap draws half the stroke width behind the start point, so we want to
    // move it forward by that amount so the arc visually appears in the correct place
    val adjustedStartAngle = startAngle + squareStrokeCapOffset

    // (STATIC-LENGTH) Set the adjustedSweep to '90f' along with having '360 for (BaseRotationAngle + JumpRotationAngle)'
    // (DYNAMIC-LENGTH) 'max(sweep, 0.1f)' => DYNAMIC
    val adjustedSweep = max(sweep, 0.1f)

    drawCircularIndicator(adjustedStartAngle, adjustedSweep, color, stroke)

}


private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}
