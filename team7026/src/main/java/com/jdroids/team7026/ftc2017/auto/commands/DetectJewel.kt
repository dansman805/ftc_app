package com.jdroids.team7026.ftc2017.auto.commands

import com.jdroids.team7026.ftc2017.Robot
import com.jdroids.team7026.ftc2017.auto.commands.commandtemplates.Command
import com.qualcomm.robotcore.util.ElapsedTime
import org.corningrobotics.enderbots.endercv.CameraViewDisplay
import org.corningrobotics.enderbots.endercv.OpenCVPipeline
import org.opencv.core.*
import org.opencv.imgproc.Imgproc


class DetectJewel: Command {
    enum class Color {
        RED, BLUE, NONE
    }

    class JewelVision: OpenCVPipeline(){
        private var hsv = Mat()
        private var thresholded = Mat()
        private var thresholded_rgba = Mat()

        private var bgrPlanes = mutableListOf<Mat>()

        private var blueData: Array<Double>? = null
        private var redData: Array<Double>? = null

        private var bHist = Mat()
        private var rHist = Mat()
        private var croppedImage = Mat()

        private var p1 = Point(336.0, 1462.0)
        private var p4 = Point(1020.0, 1914.0)

        private val histSize = MatOfInt(256)
        private val channels = MatOfInt(0)
        private val mask = Mat()

        private var rectCrop: Rect? = null

        private var range = MatOfFloat(0f, 256f)

        private var lastBlue: Int = 0
        private var lastRed: Int = 0

        private val uniform = true
        private val accumulate = false

        private var firstTimeThroughLoop = true

        private val bluePlane = mutableListOf<Mat>()
        private val redPlane = mutableListOf<Mat>()

        var jewelDetected = Color.NONE

        override fun processFrame(rgba: Mat?, gray: Mat?): Mat {
            bluePlane.clear()
            redPlane.clear()
            bgrPlanes.clear()

            croppedImage = rgba!!

            //Cropping code; causes phone to black screen and close app (needs to be commented to be able to figure out where to crop using below code)
            if (firstTimeThroughLoop) {
                rectCrop = Rect(20, 240, 105, 240)
                firstTimeThroughLoop = false
            }
            croppedImage = rgba.submat(rectCrop)
            Imgproc.resize(croppedImage, croppedImage, rgba.size())

            /*
            //Used to determine where to crop (needs to be commented once the right cropping numbers are found)
            val topLeftPoint = Point((rgba.width() * 0.0) as Int, (rgba.height() * (1.0 / 2.0)) as Int)
            val bottomRightPoint = Point(rgba.height() * (1.0 / 3.0), rgba.width())
            Imgproc.rectangle(croppedImage, topLeftPoint, bottomRightPoint, Scalar(73.0, 94.0, 49.0))
            */

            Core.split(croppedImage, bgrPlanes)

            bluePlane.add(bgrPlanes[0])
            redPlane.add(bgrPlanes[2])

            Imgproc.calcHist(bluePlane, channels, mask, bHist, histSize, range)
            Imgproc.calcHist(redPlane, channels, mask, rHist, histSize, range)

            bHist.convertTo(bHist, CvType.CV_64FC3)
            rHist.convertTo(rHist, CvType.CV_64FC3)

            blueData = Array(bHist.width() - 1, {Int -> bHist.get(bHist.width() - 1, 0)[Int]})
            redData = Array(rHist.width() - 1, {Int -> rHist.get(rHist.width() - 1, 0)[Int]})

            lastBlue = blueData!![blueData!!.size - 1].toInt()
            lastRed = redData!![redData!!.size - 1].toInt()

            when (Math.abs(lastRed - lastBlue) > 3000) {
                true -> {
                    if (lastRed > lastBlue) jewelDetected = Color.RED

                    else if (lastBlue > lastRed) jewelDetected = Color.BLUE
                }

                false -> jewelDetected = Color.NONE
            }

            //To limit the frame rate
            try {
                Thread.sleep(100)
            }
            catch (e: InterruptedException) {}


            return croppedImage
        }
    }

    private var listOfJewelColors = mutableListOf<Color>()

    private val timer = ElapsedTime()

    private var isFinished = true

    private val jewelVision = JewelVision()

    override fun initialize() {
        jewelVision.init(Robot.opMode?.hardwareMap?.appContext, CameraViewDisplay.getInstance(), 1)

        jewelVision.enable()
        timer.reset()
    }

    override fun execute() {
        if(listOfJewelColors.size > 5) {
            isFinished = true
        }
        else {
            listOfJewelColors.add(jewelVision.jewelDetected)
        }
    }

    override fun end() {
        val listOfBlueResults = listOfJewelColors.filter {it==Color.BLUE}
        val listOfRedResults = listOfJewelColors.filter {it==Color.RED}

        Robot.globalValues.jewelOnLeft = when {
            listOfBlueResults.size >= 4 -> Color.BLUE
            listOfRedResults.size >= 4 -> Color.RED
            else -> Color.NONE
        }
    }

    override fun isFinished(): Boolean {
        return isFinished
    }
}