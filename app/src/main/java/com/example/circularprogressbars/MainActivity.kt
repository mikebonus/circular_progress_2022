package com.example.circularprogressbars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.circularprogressbars.ui.DynamicCircularProgressBar
import com.example.circularprogressbars.ui.EasingCircularProgressBar
import com.example.circularprogressbars.ui.MoreStrokesCircularProgressBar
import com.example.circularprogressbars.ui.StaticCircularProgressBar
import com.example.circularprogressbars.ui.theme.CircularProgressBarSTheme

/**
 *
 *  #1) SPEED of the rotation.
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CircularProgressBarSTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {

                    EasingCircularProgressBar()
//                    StaticCircularProgressBar()
//                    DynamicCircularProgressBar()
//                    MoreStrokesCircularProgressBar()

                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 0.dp, 0.dp, 100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.test),
                            contentDescription = null,
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }
            }
        }
    }
}
