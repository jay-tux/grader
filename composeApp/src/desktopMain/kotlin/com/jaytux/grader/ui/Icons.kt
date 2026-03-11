package com.jaytux.grader.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ChevronRight: ImageVector by lazy {
    ImageVector.Builder(
        name = "ChevronRight",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = null,
            fillAlpha = 1.0f,
            stroke = SolidColor(Color(0xFF000000)),
            strokeAlpha = 1.0f,
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(9f, 18f)
            lineToRelative(6f, -6f)
            lineToRelative(-6f, -6f)
        }
    }.build()
}

val ChevronDown: ImageVector by lazy {
    ImageVector.Builder(
        name = "ChevronDown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = null,
            fillAlpha = 1.0f,
            stroke = SolidColor(Color(0xFF000000)),
            strokeAlpha = 1.0f,
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(6f, 9f)
            lineToRelative(6f, 6f)
            lineToRelative(6f, -6f)
        }
    }.build()
}

val ChevronLeft: ImageVector by lazy {
    ImageVector.Builder(
        name = "ChevronLeft",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = null,
            fillAlpha = 1.0f,
            stroke = SolidColor(Color(0xFF000000)),
            strokeAlpha = 1.0f,
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(15f, 18f)
            lineToRelative(-6f, -6f)
            lineToRelative(6f, -6f)
        }
    }.build()
}

val Delete: ImageVector by lazy {
    ImageVector.Builder(
        name = "delete",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(280f, 840f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(200f, 760f)
            verticalLineToRelative(-520f)
            horizontalLineToRelative(-40f)
            verticalLineToRelative(-80f)
            horizontalLineToRelative(200f)
            verticalLineToRelative(-40f)
            horizontalLineToRelative(240f)
            verticalLineToRelative(40f)
            horizontalLineToRelative(200f)
            verticalLineToRelative(80f)
            horizontalLineToRelative(-40f)
            verticalLineToRelative(520f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(680f, 840f)
            horizontalLineTo(280f)
            close()
            moveToRelative(400f, -600f)
            horizontalLineTo(280f)
            verticalLineToRelative(520f)
            horizontalLineToRelative(400f)
            verticalLineToRelative(-520f)
            close()
            moveTo(360f, 680f)
            horizontalLineToRelative(80f)
            verticalLineToRelative(-360f)
            horizontalLineToRelative(-80f)
            verticalLineToRelative(360f)
            close()
            moveToRelative(160f, 0f)
            horizontalLineToRelative(80f)
            verticalLineToRelative(-360f)
            horizontalLineToRelative(-80f)
            verticalLineToRelative(360f)
            close()
            moveTo(280f, 240f)
            verticalLineToRelative(520f)
            verticalLineToRelative(-520f)
            close()
        }
    }.build()
}

val CirclePlus: ImageVector by lazy {
    ImageVector.Builder(
        name = "circle-plus",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(22f, 12f)
            arcTo(10f, 10f, 0f, false, true, 12f, 22f)
            arcTo(10f, 10f, 0f, false, true, 2f, 12f)
            arcTo(10f, 10f, 0f, false, true, 22f, 12f)
            close()
        }
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(8f, 12f)
            horizontalLineToRelative(8f)
        }
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(12f, 8f)
            verticalLineToRelative(8f)
        }
    }.build()
}

val LibraryPlus: ImageVector by lazy {
    ImageVector.Builder(
        name = "library-plus",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(7f, 3f)
            moveToRelative(0f, 2.667f)
            arcToRelative(2.667f, 2.667f, 0f, false, true, 2.667f, -2.667f)
            horizontalLineToRelative(8.666f)
            arcToRelative(2.667f, 2.667f, 0f, false, true, 2.667f, 2.667f)
            verticalLineToRelative(8.666f)
            arcToRelative(2.667f, 2.667f, 0f, false, true, -2.667f, 2.667f)
            horizontalLineToRelative(-8.666f)
            arcToRelative(2.667f, 2.667f, 0f, false, true, -2.667f, -2.667f)
            close()
        }
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(4.012f, 7.26f)
            arcToRelative(2.005f, 2.005f, 0f, false, false, -1.012f, 1.737f)
            verticalLineToRelative(10f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(10f)
            curveToRelative(0.75f, 0f, 1.158f, -0.385f, 1.5f, -1f)
        }
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(11f, 10f)
            horizontalLineToRelative(6f)
        }
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(14f, 7f)
            verticalLineToRelative(6f)
        }
    }.build()
}

val Archive: ImageVector by lazy {
    ImageVector.Builder(
        name = "archive",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(480f, 720f)
            lineToRelative(160f, -160f)
            lineToRelative(-56f, -56f)
            lineToRelative(-64f, 64f)
            verticalLineToRelative(-168f)
            horizontalLineToRelative(-80f)
            verticalLineToRelative(168f)
            lineToRelative(-64f, -64f)
            lineToRelative(-56f, 56f)
            lineToRelative(160f, 160f)
            close()
            moveTo(200f, 320f)
            verticalLineToRelative(440f)
            horizontalLineToRelative(560f)
            verticalLineToRelative(-440f)
            horizontalLineTo(200f)
            close()
            moveToRelative(0f, 520f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(120f, 760f)
            verticalLineToRelative(-499f)
            quadToRelative(0f, -14f, 4.5f, -27f)
            reflectiveQuadToRelative(13.5f, -24f)
            lineToRelative(50f, -61f)
            quadToRelative(11f, -14f, 27.5f, -21.5f)
            reflectiveQuadTo(250f, 120f)
            horizontalLineToRelative(460f)
            quadToRelative(18f, 0f, 34.5f, 7.5f)
            reflectiveQuadTo(772f, 149f)
            lineToRelative(50f, 61f)
            quadToRelative(9f, 11f, 13.5f, 24f)
            reflectiveQuadToRelative(4.5f, 27f)
            verticalLineToRelative(499f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(760f, 840f)
            horizontalLineTo(200f)
            close()
            moveToRelative(16f, -600f)
            horizontalLineToRelative(528f)
            lineToRelative(-34f, -40f)
            horizontalLineTo(250f)
            lineToRelative(-34f, 40f)
            close()
            moveToRelative(264f, 300f)
            close()
        }
    }.build()
}

val Unarchive: ImageVector by lazy {
    ImageVector.Builder(
        name = "unarchive",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent)
        ) {
            moveTo(0f, 0f)
            horizontalLineToRelative(24f)
            verticalLineToRelative(24f)
            horizontalLineTo(0f)
            verticalLineTo(0f)
            close()
        }
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(20.54f, 5.23f)
            lineToRelative(-1.39f, -1.68f)
            curveTo(18.88f, 3.21f, 18.47f, 3f, 18f, 3f)
            horizontalLineTo(6f)
            curveToRelative(-0.47f, 0f, -0.88f, 0.21f, -1.16f, 0.55f)
            lineTo(3.46f, 5.23f)
            curveTo(3.17f, 5.57f, 3f, 6.02f, 3f, 6.5f)
            verticalLineTo(19f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(14f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            verticalLineTo(6.5f)
            curveToRelative(0f, -0.48f, -0.17f, -0.93f, -0.46f, -1.27f)
            close()
            moveTo(6.24f, 5f)
            horizontalLineToRelative(11.52f)
            lineToRelative(0.83f, 1f)
            horizontalLineTo(5.42f)
            lineToRelative(0.82f, -1f)
            close()
            moveTo(5f, 19f)
            verticalLineTo(8f)
            horizontalLineToRelative(14f)
            verticalLineToRelative(11f)
            horizontalLineTo(5f)
            close()
            moveToRelative(3f, -5f)
            horizontalLineToRelative(2.55f)
            verticalLineToRelative(3f)
            horizontalLineToRelative(2.9f)
            verticalLineToRelative(-3f)
            horizontalLineTo(16f)
            lineToRelative(-4f, -4f)
            close()
        }
    }.build()
}

val FormatSize: ImageVector by lazy {
    ImageVector.Builder(
        name = "format_size",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent)
        ) {
            moveTo(0f, 0f)
            horizontalLineToRelative(24f)
            verticalLineToRelative(24f)
            horizontalLineTo(0f)
            verticalLineTo(0f)
            close()
        }
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(9f, 4f)
            verticalLineToRelative(3f)
            horizontalLineToRelative(5f)
            verticalLineToRelative(12f)
            horizontalLineToRelative(3f)
            verticalLineTo(7f)
            horizontalLineToRelative(5f)
            verticalLineTo(4f)
            horizontalLineTo(9f)
            close()
            moveToRelative(-6f, 8f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(7f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(-7f)
            horizontalLineToRelative(3f)
            verticalLineTo(9f)
            horizontalLineTo(3f)
            verticalLineToRelative(3f)
            close()
        }
    }.build()
}

val CircleFilled: ImageVector by lazy {
    ImageVector.Builder(
        name = "circle-large-filled",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 16f,
        viewportHeight = 16f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(8f, 1f)
            curveTo(8.64258f, 1f, 9.26237f, 1.08431f, 9.85938f, 1.25293f)
            curveTo(10.4564f, 1.41699f, 11.0124f, 1.65169f, 11.5273f, 1.95703f)
            curveTo(12.0469f, 2.26237f, 12.5186f, 2.62923f, 12.9424f, 3.05762f)
            curveTo(13.3708f, 3.48145f, 13.7376f, 3.95312f, 14.043f, 4.47266f)
            curveTo(14.3483f, 4.98763f, 14.583f, 5.54362f, 14.7471f, 6.14062f)
            curveTo(14.9157f, 6.73763f, 15f, 7.35742f, 15f, 8f)
            curveTo(15f, 8.64258f, 14.9157f, 9.26237f, 14.7471f, 9.85938f)
            curveTo(14.583f, 10.4564f, 14.3483f, 11.0146f, 14.043f, 11.5342f)
            curveTo(13.7376f, 12.0492f, 13.3708f, 12.5208f, 12.9424f, 12.9492f)
            curveTo(12.5186f, 13.373f, 12.0469f, 13.7376f, 11.5273f, 14.043f)
            curveTo(11.0124f, 14.3483f, 10.4564f, 14.5853f, 9.85938f, 14.7539f)
            curveTo(9.26237f, 14.918f, 8.64258f, 15f, 8f, 15f)
            curveTo(7.35742f, 15f, 6.73763f, 14.918f, 6.14062f, 14.7539f)
            curveTo(5.54362f, 14.5853f, 4.98535f, 14.3483f, 4.46582f, 14.043f)
            curveTo(3.95085f, 13.7376f, 3.47917f, 13.373f, 3.05078f, 12.9492f)
            curveTo(2.62695f, 12.5208f, 2.26237f, 12.0492f, 1.95703f, 11.5342f)
            curveTo(1.65169f, 11.0146f, 1.41471f, 10.4564f, 1.24609f, 9.85938f)
            curveTo(1.08203f, 9.26237f, 1f, 8.64258f, 1f, 8f)
            curveTo(1f, 7.35742f, 1.08203f, 6.73763f, 1.24609f, 6.14062f)
            curveTo(1.41471f, 5.54362f, 1.65169f, 4.98763f, 1.95703f, 4.47266f)
            curveTo(2.26237f, 3.95312f, 2.62695f, 3.48145f, 3.05078f, 3.05762f)
            curveTo(3.47917f, 2.62923f, 3.95085f, 2.26237f, 4.46582f, 1.95703f)
            curveTo(4.98535f, 1.65169f, 5.54362f, 1.41699f, 6.14062f, 1.25293f)
            curveTo(6.73763f, 1.08431f, 7.35742f, 1f, 8f, 1f)
            close()
        }
    }.build()
}

val CircleOutline: ImageVector by lazy {
    ImageVector.Builder(
        name = "circle-large",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 16f,
        viewportHeight = 16f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(9.58854f, 2.2153f)
            lineTo(9.58854f, 2.21528f)
            curveTo(9.08311f, 2.07252f, 8.55496f, 2f, 8.00098f, 2f)
            curveTo(7.44699f, 2f, 6.91884f, 2.07252f, 6.41341f, 2.21528f)
            lineTo(6.40659f, 2.21721f)
            lineTo(6.40659f, 2.21718f)
            curveTo(5.89259f, 2.35843f, 5.41641f, 2.55904f, 4.97513f, 2.81819f)
            curveTo(4.5335f, 3.08023f, 4.12844f, 3.39515f, 3.75886f, 3.76472f)
            curveTo(3.39532f, 4.12826f, 3.08274f, 4.53252f, 2.82014f, 4.97935f)
            lineTo(2.81818f, 4.98267f)
            lineTo(2.81818f, 4.98267f)
            curveTo(2.55924f, 5.41938f, 2.35622f, 5.89379f, 2.21039f, 6.409f)
            curveTo(2.07155f, 6.91543f, 2.00098f, 7.44479f, 2.00098f, 8f)
            curveTo(2.00098f, 8.5552f, 2.07155f, 9.08455f, 2.21038f, 9.59097f)
            curveTo(2.35622f, 10.1062f, 2.55946f, 10.5836f, 2.81921f, 11.0259f)
            curveTo(3.08148f, 11.4679f, 3.39423f, 11.8729f, 3.75887f, 12.2421f)
            curveTo(4.12806f, 12.6068f, 4.53308f, 12.9195f, 4.97513f, 13.1818f)
            curveTo(5.41737f, 13.4415f, 5.89472f, 13.6447f, 6.40994f, 13.7906f)
            curveTo(6.91638f, 13.9294f, 7.44575f, 14f, 8.00098f, 14f)
            curveTo(8.55619f, 14f, 9.08555f, 13.9294f, 9.59198f, 13.7906f)
            curveTo(10.1072f, 13.6448f, 10.5816f, 13.4417f, 11.0183f, 13.1828f)
            lineTo(11.0216f, 13.1808f)
            lineTo(11.0216f, 13.1808f)
            curveTo(11.4685f, 12.9182f, 11.8727f, 12.6057f, 12.2363f, 12.2421f)
            curveTo(12.6058f, 11.8726f, 12.9207f, 11.4675f, 13.1827f, 11.0259f)
            curveTo(13.4419f, 10.5846f, 13.6425f, 10.1084f, 13.7838f, 9.59439f)
            lineTo(13.7857f, 9.58756f)
            lineTo(13.7857f, 9.58757f)
            curveTo(13.9285f, 9.08213f, 14.001f, 8.55398f, 14.001f, 8f)
            curveTo(14.001f, 7.44602f, 13.9285f, 6.91787f, 13.7857f, 6.41243f)
            lineTo(13.7838f, 6.40562f)
            lineTo(13.7838f, 6.40561f)
            curveTo(13.6425f, 5.8916f, 13.4421f, 5.41838f, 13.1838f, 4.98267f)
            lineTo(13.1818f, 4.97935f)
            lineTo(13.1818f, 4.97935f)
            curveTo(12.9204f, 4.53447f, 12.6069f, 4.13142f, 12.24f, 3.7685f)
            lineTo(12.2324f, 3.76097f)
            lineTo(12.2325f, 3.76093f)
            curveTo(11.8696f, 3.3941f, 11.4665f, 3.08062f, 11.0216f, 2.81916f)
            lineTo(11.0183f, 2.81721f)
            lineTo(11.0183f, 2.8172f)
            curveTo(10.5826f, 2.55885f, 10.1094f, 2.35844f, 9.59537f, 2.21718f)
            lineTo(9.58854f, 2.2153f)
            close()
            moveTo(14.0439f, 11.5342f)
            curveTo(13.7386f, 12.0492f, 13.3717f, 12.5208f, 12.9434f, 12.9492f)
            curveTo(12.5195f, 13.373f, 12.0479f, 13.7376f, 11.5283f, 14.043f)
            curveTo(11.0133f, 14.3483f, 10.4574f, 14.5853f, 9.86035f, 14.7539f)
            curveTo(9.26335f, 14.918f, 8.64355f, 15f, 8.00098f, 15f)
            curveTo(7.3584f, 15f, 6.73861f, 14.918f, 6.1416f, 14.7539f)
            curveTo(5.5446f, 14.5853f, 4.98633f, 14.3483f, 4.4668f, 14.043f)
            curveTo(3.95182f, 13.7376f, 3.48014f, 13.373f, 3.05176f, 12.9492f)
            curveTo(2.62793f, 12.5208f, 2.26335f, 12.0492f, 1.95801f, 11.5342f)
            curveTo(1.65267f, 11.0146f, 1.41569f, 10.4564f, 1.24707f, 9.85938f)
            curveTo(1.08301f, 9.26237f, 1.00098f, 8.64258f, 1.00098f, 8f)
            curveTo(1.00098f, 7.35742f, 1.08301f, 6.73763f, 1.24707f, 6.14062f)
            curveTo(1.41569f, 5.54362f, 1.65267f, 4.98763f, 1.95801f, 4.47266f)
            curveTo(2.26335f, 3.95312f, 2.62793f, 3.48145f, 3.05176f, 3.05762f)
            curveTo(3.48014f, 2.62923f, 3.95182f, 2.26237f, 4.4668f, 1.95703f)
            curveTo(4.98633f, 1.65169f, 5.5446f, 1.41699f, 6.1416f, 1.25293f)
            curveTo(6.73861f, 1.08431f, 7.3584f, 1f, 8.00098f, 1f)
            curveTo(8.64355f, 1f, 9.26335f, 1.08431f, 9.86035f, 1.25293f)
            curveTo(10.4574f, 1.41699f, 11.0133f, 1.65169f, 11.5283f, 1.95703f)
            curveTo(12.0479f, 2.26237f, 12.5195f, 2.62923f, 12.9434f, 3.05762f)
            curveTo(13.3717f, 3.48145f, 13.7386f, 3.95312f, 14.0439f, 4.47266f)
            curveTo(14.3493f, 4.98763f, 14.584f, 5.54362f, 14.748f, 6.14062f)
            curveTo(14.9167f, 6.73763f, 15.001f, 7.35742f, 15.001f, 8f)
            curveTo(15.001f, 8.64258f, 14.9167f, 9.26237f, 14.748f, 9.85938f)
            curveTo(14.584f, 10.4564f, 14.3493f, 11.0146f, 14.0439f, 11.5342f)
            close()
        }
    }.build()
}

val FormatListBullet: ImageVector by lazy {
    ImageVector.Builder(
        name = "format_list_bulleted",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent)
        ) {
            moveTo(0f, 0f)
            horizontalLineToRelative(24f)
            verticalLineToRelative(24f)
            horizontalLineTo(0f)
            verticalLineTo(0f)
            close()
        }
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(4f, 10.5f)
            curveToRelative(-0.83f, 0f, -1.5f, 0.67f, -1.5f, 1.5f)
            reflectiveCurveToRelative(0.67f, 1.5f, 1.5f, 1.5f)
            reflectiveCurveToRelative(1.5f, -0.67f, 1.5f, -1.5f)
            reflectiveCurveToRelative(-0.67f, -1.5f, -1.5f, -1.5f)
            close()
            moveToRelative(0f, -6f)
            curveToRelative(-0.83f, 0f, -1.5f, 0.67f, -1.5f, 1.5f)
            reflectiveCurveTo(3.17f, 7.5f, 4f, 7.5f)
            reflectiveCurveTo(5.5f, 6.83f, 5.5f, 6f)
            reflectiveCurveTo(4.83f, 4.5f, 4f, 4.5f)
            close()
            moveToRelative(0f, 12f)
            curveToRelative(-0.83f, 0f, -1.5f, 0.68f, -1.5f, 1.5f)
            reflectiveCurveToRelative(0.68f, 1.5f, 1.5f, 1.5f)
            reflectiveCurveToRelative(1.5f, -0.68f, 1.5f, -1.5f)
            reflectiveCurveToRelative(-0.67f, -1.5f, -1.5f, -1.5f)
            close()
            moveTo(7f, 19f)
            horizontalLineToRelative(14f)
            verticalLineToRelative(-2f)
            horizontalLineTo(7f)
            verticalLineToRelative(2f)
            close()
            moveToRelative(0f, -6f)
            horizontalLineToRelative(14f)
            verticalLineToRelative(-2f)
            horizontalLineTo(7f)
            verticalLineToRelative(2f)
            close()
            moveToRelative(0f, -8f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(14f)
            verticalLineTo(5f)
            horizontalLineTo(7f)
            close()
        }
    }.build()
}

val FormatListNumber: ImageVector by lazy {
    ImageVector.Builder(
        name = "format_list_numbered",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent)
        ) {
            moveTo(0f, 0f)
            horizontalLineToRelative(24f)
            verticalLineToRelative(24f)
            horizontalLineTo(0f)
            verticalLineTo(0f)
            close()
        }
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(2f, 17f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(0.5f)
            horizontalLineTo(3f)
            verticalLineToRelative(1f)
            horizontalLineToRelative(1f)
            verticalLineToRelative(0.5f)
            horizontalLineTo(2f)
            verticalLineToRelative(1f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(-4f)
            horizontalLineTo(2f)
            verticalLineToRelative(1f)
            close()
            moveToRelative(1f, -9f)
            horizontalLineToRelative(1f)
            verticalLineTo(4f)
            horizontalLineTo(2f)
            verticalLineToRelative(1f)
            horizontalLineToRelative(1f)
            verticalLineToRelative(3f)
            close()
            moveToRelative(-1f, 3f)
            horizontalLineToRelative(1.8f)
            lineTo(2f, 13.1f)
            verticalLineToRelative(0.9f)
            horizontalLineToRelative(3f)
            verticalLineToRelative(-1f)
            horizontalLineTo(3.2f)
            lineTo(5f, 10.9f)
            verticalLineTo(10f)
            horizontalLineTo(2f)
            verticalLineToRelative(1f)
            close()
            moveToRelative(5f, -6f)
            verticalLineToRelative(2f)
            horizontalLineToRelative(14f)
            verticalLineTo(5f)
            horizontalLineTo(7f)
            close()
            moveToRelative(0f, 14f)
            horizontalLineToRelative(14f)
            verticalLineToRelative(-2f)
            horizontalLineTo(7f)
            verticalLineToRelative(2f)
            close()
            moveToRelative(0f, -6f)
            horizontalLineToRelative(14f)
            verticalLineToRelative(-2f)
            horizontalLineTo(7f)
            verticalLineToRelative(2f)
            close()
        }
    }.build()
}

val FormatCode: ImageVector by lazy {
    ImageVector.Builder(
        name = "code",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 640f,
        viewportHeight = 512f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(278.9f, 511.5f)
            lineToRelative(-61f, -17.7f)
            curveToRelative(-6.4f, -1.8f, -10f, -8.5f, -8.2f, -14.9f)
            lineTo(346.2f, 8.7f)
            curveToRelative(1.8f, -6.4f, 8.5f, -10f, 14.9f, -8.2f)
            lineToRelative(61f, 17.7f)
            curveToRelative(6.4f, 1.8f, 10f, 8.5f, 8.2f, 14.9f)
            lineTo(293.8f, 503.3f)
            curveToRelative(-1.9f, 6.4f, -8.5f, 10.1f, -14.9f, 8.2f)
            close()
            moveToRelative(-114f, -112.2f)
            lineToRelative(43.5f, -46.4f)
            curveToRelative(4.6f, -4.9f, 4.3f, -12.7f, -0.8f, -17.2f)
            lineTo(117f, 256f)
            lineToRelative(90.6f, -79.7f)
            curveToRelative(5.1f, -4.5f, 5.5f, -12.3f, 0.8f, -17.2f)
            lineToRelative(-43.5f, -46.4f)
            curveToRelative(-4.5f, -4.8f, -12.1f, -5.1f, -17f, -0.5f)
            lineTo(3.8f, 247.2f)
            curveToRelative(-5.1f, 4.7f, -5.1f, 12.8f, 0f, 17.5f)
            lineToRelative(144.1f, 135.1f)
            curveToRelative(4.9f, 4.6f, 12.5f, 4.4f, 17f, -0.5f)
            close()
            moveToRelative(327.2f, 0.6f)
            lineToRelative(144.1f, -135.1f)
            curveToRelative(5.1f, -4.7f, 5.1f, -12.8f, 0f, -17.5f)
            lineTo(492.1f, 112.1f)
            curveToRelative(-4.8f, -4.5f, -12.4f, -4.3f, -17f, 0.5f)
            lineTo(431.6f, 159f)
            curveToRelative(-4.6f, 4.9f, -4.3f, 12.7f, 0.8f, 17.2f)
            lineTo(523f, 256f)
            lineToRelative(-90.6f, 79.7f)
            curveToRelative(-5.1f, 4.5f, -5.5f, 12.3f, -0.8f, 17.2f)
            lineToRelative(43.5f, 46.4f)
            curveToRelative(4.5f, 4.9f, 12.1f, 5.1f, 17f, 0.6f)
            close()
        }
    }.build()
}

val ContentCopy: ImageVector by lazy {
    ImageVector.Builder(
        name = "content_copy",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent)
        ) {
            moveTo(0f, 0f)
            horizontalLineToRelative(24f)
            verticalLineToRelative(24f)
            horizontalLineTo(0f)
            verticalLineTo(0f)
            close()
        }
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(16f, 1f)
            horizontalLineTo(4f)
            curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
            verticalLineToRelative(14f)
            horizontalLineToRelative(2f)
            verticalLineTo(3f)
            horizontalLineToRelative(12f)
            verticalLineTo(1f)
            close()
            moveToRelative(3f, 4f)
            horizontalLineTo(8f)
            curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
            verticalLineToRelative(14f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(11f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            verticalLineTo(7f)
            curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
            close()
            moveToRelative(0f, 16f)
            horizontalLineTo(8f)
            verticalLineTo(7f)
            horizontalLineToRelative(11f)
            verticalLineToRelative(14f)
            close()
        }
    }.build()
}

val ContentPaste: ImageVector by lazy {
    ImageVector.Builder(
        name = "content_paste",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent)
        ) {
            moveTo(0f, 0f)
            horizontalLineToRelative(24f)
            verticalLineToRelative(24f)
            horizontalLineTo(0f)
            verticalLineTo(0f)
            close()
        }
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(19f, 2f)
            horizontalLineToRelative(-4.18f)
            curveTo(14.4f, 0.84f, 13.3f, 0f, 12f, 0f)
            reflectiveCurveTo(9.6f, 0.84f, 9.18f, 2f)
            horizontalLineTo(5f)
            curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
            verticalLineToRelative(16f)
            curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
            horizontalLineToRelative(14f)
            curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
            verticalLineTo(4f)
            curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
            close()
            moveToRelative(-7f, 0f)
            curveToRelative(0.55f, 0f, 1f, 0.45f, 1f, 1f)
            reflectiveCurveToRelative(-0.45f, 1f, -1f, 1f)
            reflectiveCurveToRelative(-1f, -0.45f, -1f, -1f)
            reflectiveCurveToRelative(0.45f, -1f, 1f, -1f)
            close()
            moveToRelative(7f, 18f)
            horizontalLineTo(5f)
            verticalLineTo(4f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(3f)
            horizontalLineToRelative(10f)
            verticalLineTo(4f)
            horizontalLineToRelative(2f)
            verticalLineToRelative(16f)
            close()
        }
    }.build()
}

val FormatItalic: ImageVector by lazy {
    ImageVector.Builder(
        name = "italic",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 320f,
        viewportHeight = 512f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(320f, 48f)
            verticalLineToRelative(32f)
            arcToRelative(16f, 16f, 0f, false, true, -16f, 16f)
            horizontalLineToRelative(-62.76f)
            lineToRelative(-80f, 320f)
            horizontalLineTo(208f)
            arcToRelative(16f, 16f, 0f, false, true, 16f, 16f)
            verticalLineToRelative(32f)
            arcToRelative(16f, 16f, 0f, false, true, -16f, 16f)
            horizontalLineTo(16f)
            arcToRelative(16f, 16f, 0f, false, true, -16f, -16f)
            verticalLineToRelative(-32f)
            arcToRelative(16f, 16f, 0f, false, true, 16f, -16f)
            horizontalLineToRelative(62.76f)
            lineToRelative(80f, -320f)
            horizontalLineTo(112f)
            arcToRelative(16f, 16f, 0f, false, true, -16f, -16f)
            verticalLineTo(48f)
            arcToRelative(16f, 16f, 0f, false, true, 16f, -16f)
            horizontalLineToRelative(192f)
            arcToRelative(16f, 16f, 0f, false, true, 16f, 16f)
            close()
        }
    }.build()
}

val FormatBold: ImageVector by lazy {
    ImageVector.Builder(
        name = "bold",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 384f,
        viewportHeight = 512f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(333.49f, 238f)
            arcToRelative(122f, 122f, 0f, false, false, 27f, -65.21f)
            curveTo(367.87f, 96.49f, 308f, 32f, 233.42f, 32f)
            horizontalLineTo(34f)
            arcToRelative(16f, 16f, 0f, false, false, -16f, 16f)
            verticalLineToRelative(48f)
            arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
            horizontalLineToRelative(31.87f)
            verticalLineToRelative(288f)
            horizontalLineTo(34f)
            arcToRelative(16f, 16f, 0f, false, false, -16f, 16f)
            verticalLineToRelative(48f)
            arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
            horizontalLineToRelative(209.32f)
            curveToRelative(70.8f, 0f, 134.14f, -51.75f, 141f, -122.4f)
            curveToRelative(4.74f, -48.45f, -16.39f, -92.06f, -50.83f, -119.6f)
            close()
            moveTo(145.66f, 112f)
            horizontalLineToRelative(87.76f)
            arcToRelative(48f, 48f, 0f, false, true, 0f, 96f)
            horizontalLineToRelative(-87.76f)
            close()
            moveToRelative(87.76f, 288f)
            horizontalLineToRelative(-87.76f)
            verticalLineTo(288f)
            horizontalLineToRelative(87.76f)
            arcToRelative(56f, 56f, 0f, false, true, 0f, 112f)
            close()
        }
    }.build()
}

val FormatUnderline: ImageVector by lazy {
    ImageVector.Builder(
        name = "underline",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 448f,
        viewportHeight = 512f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(32f, 64f)
            horizontalLineToRelative(32f)
            verticalLineToRelative(160f)
            curveToRelative(0f, 88.22f, 71.78f, 160f, 160f, 160f)
            reflectiveCurveToRelative(160f, -71.78f, 160f, -160f)
            verticalLineTo(64f)
            horizontalLineToRelative(32f)
            arcToRelative(16f, 16f, 0f, false, false, 16f, -16f)
            verticalLineTo(16f)
            arcToRelative(16f, 16f, 0f, false, false, -16f, -16f)
            horizontalLineTo(272f)
            arcToRelative(16f, 16f, 0f, false, false, -16f, 16f)
            verticalLineToRelative(32f)
            arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
            horizontalLineToRelative(32f)
            verticalLineToRelative(160f)
            arcToRelative(80f, 80f, 0f, false, true, -160f, 0f)
            verticalLineTo(64f)
            horizontalLineToRelative(32f)
            arcToRelative(16f, 16f, 0f, false, false, 16f, -16f)
            verticalLineTo(16f)
            arcToRelative(16f, 16f, 0f, false, false, -16f, -16f)
            horizontalLineTo(32f)
            arcToRelative(16f, 16f, 0f, false, false, -16f, 16f)
            verticalLineToRelative(32f)
            arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
            close()
            moveToRelative(400f, 384f)
            horizontalLineTo(16f)
            arcToRelative(16f, 16f, 0f, false, false, -16f, 16f)
            verticalLineToRelative(32f)
            arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
            horizontalLineToRelative(416f)
            arcToRelative(16f, 16f, 0f, false, false, 16f, -16f)
            verticalLineToRelative(-32f)
            arcToRelative(16f, 16f, 0f, false, false, -16f, -16f)
            close()
        }
    }.build()
}

val FormatStrikethrough: ImageVector by lazy {
    ImageVector.Builder(
        name = "strikethrough",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 512f,
        viewportHeight = 512f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(496f, 224f)
            horizontalLineTo(293.9f)
            lineToRelative(-87.17f, -26.83f)
            arcTo(43.55f, 43.55f, 0f, false, true, 219.55f, 112f)
            horizontalLineToRelative(66.79f)
            arcTo(49.89f, 49.89f, 0f, false, true, 331f, 139.58f)
            arcToRelative(16f, 16f, 0f, false, false, 21.46f, 7.15f)
            lineToRelative(42.94f, -21.47f)
            arcToRelative(16f, 16f, 0f, false, false, 7.16f, -21.46f)
            lineToRelative(-0.53f, -1f)
            arcTo(128f, 128f, 0f, false, false, 287.51f, 32f)
            horizontalLineToRelative(-68f)
            arcToRelative(123.68f, 123.68f, 0f, false, false, -123f, 135.64f)
            curveToRelative(2f, 20.89f, 10.1f, 39.83f, 21.78f, 56.36f)
            horizontalLineTo(16f)
            arcToRelative(16f, 16f, 0f, false, false, -16f, 16f)
            verticalLineToRelative(32f)
            arcToRelative(16f, 16f, 0f, false, false, 16f, 16f)
            horizontalLineToRelative(480f)
            arcToRelative(16f, 16f, 0f, false, false, 16f, -16f)
            verticalLineToRelative(-32f)
            arcToRelative(16f, 16f, 0f, false, false, -16f, -16f)
            close()
            moveToRelative(-180.24f, 96f)
            arcTo(43f, 43f, 0f, false, true, 336f, 356.45f)
            arcTo(43.59f, 43.59f, 0f, false, true, 292.45f, 400f)
            horizontalLineToRelative(-66.79f)
            arcTo(49.89f, 49.89f, 0f, false, true, 181f, 372.42f)
            arcToRelative(16f, 16f, 0f, false, false, -21.46f, -7.15f)
            lineToRelative(-42.94f, 21.47f)
            arcToRelative(16f, 16f, 0f, false, false, -7.16f, 21.46f)
            lineToRelative(0.53f, 1f)
            arcTo(128f, 128f, 0f, false, false, 224.49f, 480f)
            horizontalLineToRelative(68f)
            arcToRelative(123.68f, 123.68f, 0f, false, false, 123f, -135.64f)
            arcToRelative(114.25f, 114.25f, 0f, false, false, -5.34f, -24.36f)
            close()
        }
    }.build()
}

val UserIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "user",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.5f,
            strokeLineJoin = StrokeJoin.Miter
        ) {
            moveTo(15.75f, 6f)
            arcToRelative(3.75f, 3.75f, 0f, true, true, -7.5f, 0f)
            arcToRelative(3.75f, 3.75f, 0f, false, true, 7.5f, 0f)
            close()
            moveTo(4.501f, 20.118f)
            arcToRelative(7.5f, 7.5f, 0f, false, true, 14.998f, 0f)
            arcTo(17.933f, 17.933f, 0f, false, true, 12f, 21.75f)
            curveToRelative(-2.676f, 0f, -5.216f, -0.584f, -7.499f, -1.632f)
            close()
        }
    }.build()
}

val UserGroupIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "user-group",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.5f,
            strokeLineJoin = StrokeJoin.Miter
        ) {
            moveTo(18f, 18.72f)
            arcToRelative(9.094f, 9.094f, 0f, false, false, 3.741f, -0.479f)
            arcToRelative(3f, 3f, 0f, false, false, -4.682f, -2.72f)
            moveToRelative(0.94f, 3.198f)
            lineToRelative(0.001f, 0.031f)
            curveToRelative(0f, 0.225f, -0.012f, 0.447f, -0.037f, 0.666f)
            arcTo(11.944f, 11.944f, 0f, false, true, 12f, 21f)
            curveToRelative(-2.17f, 0f, -4.207f, -0.576f, -5.963f, -1.584f)
            arcTo(6.062f, 6.062f, 0f, false, true, 6f, 18.719f)
            moveToRelative(12f, 0f)
            arcToRelative(5.971f, 5.971f, 0f, false, false, -0.941f, -3.197f)
            moveToRelative(0f, 0f)
            arcTo(5.995f, 5.995f, 0f, false, false, 12f, 12.75f)
            arcToRelative(5.995f, 5.995f, 0f, false, false, -5.058f, 2.772f)
            moveToRelative(0f, 0f)
            arcToRelative(3f, 3f, 0f, false, false, -4.681f, 2.72f)
            arcToRelative(8.986f, 8.986f, 0f, false, false, 3.74f, 0.477f)
            moveToRelative(0.94f, -3.197f)
            arcToRelative(5.971f, 5.971f, 0f, false, false, -0.94f, 3.197f)
            moveTo(15f, 6.75f)
            arcToRelative(3f, 3f, 0f, true, true, -6f, 0f)
            arcToRelative(3f, 3f, 0f, false, true, 6f, 0f)
            close()
            moveToRelative(6f, 3f)
            arcToRelative(2.25f, 2.25f, 0f, true, true, -4.5f, 0f)
            arcToRelative(2.25f, 2.25f, 0f, false, true, 4.5f, 0f)
            close()
            moveToRelative(-13.5f, 0f)
            arcToRelative(2.25f, 2.25f, 0f, true, true, -4.5f, 0f)
            arcToRelative(2.25f, 2.25f, 0f, false, true, 4.5f, 0f)
            close()
        }
    }.build()
}

val AssignmentIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "assignment",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 960f,
        viewportHeight = 960f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(200f, 840f)
            quadToRelative(-33f, 0f, -56.5f, -23.5f)
            reflectiveQuadTo(120f, 760f)
            verticalLineToRelative(-560f)
            quadToRelative(0f, -33f, 23.5f, -56.5f)
            reflectiveQuadTo(200f, 120f)
            horizontalLineToRelative(168f)
            quadToRelative(13f, -36f, 43.5f, -58f)
            reflectiveQuadToRelative(68.5f, -22f)
            quadToRelative(38f, 0f, 68.5f, 22f)
            reflectiveQuadToRelative(43.5f, 58f)
            horizontalLineToRelative(168f)
            quadToRelative(33f, 0f, 56.5f, 23.5f)
            reflectiveQuadTo(840f, 200f)
            verticalLineToRelative(560f)
            quadToRelative(0f, 33f, -23.5f, 56.5f)
            reflectiveQuadTo(760f, 840f)
            horizontalLineTo(200f)
            close()
            moveToRelative(0f, -80f)
            horizontalLineToRelative(560f)
            verticalLineToRelative(-560f)
            horizontalLineTo(200f)
            verticalLineToRelative(560f)
            close()
            moveToRelative(80f, -80f)
            horizontalLineToRelative(280f)
            verticalLineToRelative(-80f)
            horizontalLineTo(280f)
            verticalLineToRelative(80f)
            close()
            moveToRelative(0f, -160f)
            horizontalLineToRelative(400f)
            verticalLineToRelative(-80f)
            horizontalLineTo(280f)
            verticalLineToRelative(80f)
            close()
            moveToRelative(0f, -160f)
            horizontalLineToRelative(400f)
            verticalLineToRelative(-80f)
            horizontalLineTo(280f)
            verticalLineToRelative(80f)
            close()
            moveToRelative(200f, -190f)
            quadToRelative(13f, 0f, 21.5f, -8.5f)
            reflectiveQuadTo(510f, 140f)
            quadToRelative(0f, -13f, -8.5f, -21.5f)
            reflectiveQuadTo(480f, 110f)
            quadToRelative(-13f, 0f, -21.5f, 8.5f)
            reflectiveQuadTo(450f, 140f)
            quadToRelative(0f, 13f, 8.5f, 21.5f)
            reflectiveQuadTo(480f, 170f)
            close()
            moveTo(200f, 760f)
            verticalLineToRelative(-560f)
            verticalLineToRelative(560f)
            close()
        }
    }.build()
}

val Edit: ImageVector by lazy {
    ImageVector.Builder(
        name = "edit",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(11f, 4f)
            horizontalLineTo(4f)
            arcToRelative(2f, 2f, 0f, false, false, -2f, 2f)
            verticalLineToRelative(14f)
            arcToRelative(2f, 2f, 0f, false, false, 2f, 2f)
            horizontalLineToRelative(14f)
            arcToRelative(2f, 2f, 0f, false, false, 2f, -2f)
            verticalLineToRelative(-7f)
        }
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(18.5f, 2.5f)
            arcToRelative(2.121f, 2.121f, 0f, false, true, 3f, 3f)
            lineTo(12f, 15f)
            lineToRelative(-4f, 1f)
            lineToRelative(1f, -4f)
            lineToRelative(9.5f, -9.5f)
            close()
        }
    }.build()
}

val Check: ImageVector by lazy {
    ImageVector.Builder(
        name = "check",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.Transparent),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(20f, 6f)
            lineTo(9f, 17f)
            lineToRelative(-5f, -5f)
        }
    }.build()
}

val Close: ImageVector by lazy {
    ImageVector.Builder(
        name = "close",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 16f,
        viewportHeight = 16f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(8.70701f, 8.00001f)
            lineTo(12.353f, 4.35401f)
            curveTo(12.548f, 4.15901f, 12.548f, 3.84201f, 12.353f, 3.64701f)
            curveTo(12.158f, 3.45201f, 11.841f, 3.45201f, 11.646f, 3.64701f)
            lineTo(8.00001f, 7.29301f)
            lineTo(4.35401f, 3.64701f)
            curveTo(4.15901f, 3.45201f, 3.84201f, 3.45201f, 3.64701f, 3.64701f)
            curveTo(3.45201f, 3.84201f, 3.45201f, 4.15901f, 3.64701f, 4.35401f)
            lineTo(7.29301f, 8.00001f)
            lineTo(3.64701f, 11.646f)
            curveTo(3.45201f, 11.841f, 3.45201f, 12.158f, 3.64701f, 12.353f)
            curveTo(3.74501f, 12.451f, 3.87301f, 12.499f, 4.00101f, 12.499f)
            curveTo(4.12901f, 12.499f, 4.25701f, 12.45f, 4.35501f, 12.353f)
            lineTo(8.00101f, 8.70701f)
            lineTo(11.647f, 12.353f)
            curveTo(11.745f, 12.451f, 11.873f, 12.499f, 12.001f, 12.499f)
            curveTo(12.129f, 12.499f, 12.257f, 12.45f, 12.355f, 12.353f)
            curveTo(12.55f, 12.158f, 12.55f, 11.841f, 12.355f, 11.646f)
            lineTo(8.70901f, 8.00001f)
            horizontalLineTo(8.70701f)
            close()
        }
    }.build()
}

val PersonMinus: ImageVector by lazy {
    ImageVector.Builder(
        name = "person-dash",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 16f,
        viewportHeight = 16f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(12.5f, 16f)
            arcToRelative(3.5f, 3.5f, 0f, true, false, 0f, -7f)
            arcToRelative(3.5f, 3.5f, 0f, false, false, 0f, 7f)
            moveTo(11f, 12f)
            horizontalLineToRelative(3f)
            arcToRelative(0.5f, 0.5f, 0f, false, true, 0f, 1f)
            horizontalLineToRelative(-3f)
            arcToRelative(0.5f, 0.5f, 0f, false, true, 0f, -1f)
            moveToRelative(0f, -7f)
            arcToRelative(3f, 3f, 0f, true, true, -6f, 0f)
            arcToRelative(3f, 3f, 0f, false, true, 6f, 0f)
            moveTo(8f, 7f)
            arcToRelative(2f, 2f, 0f, true, false, 0f, -4f)
            arcToRelative(2f, 2f, 0f, false, false, 0f, 4f)
        }
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(8.256f, 14f)
            arcToRelative(4.5f, 4.5f, 0f, false, true, -0.229f, -1.004f)
            horizontalLineTo(3f)
            curveToRelative(0.001f, -0.246f, 0.154f, -0.986f, 0.832f, -1.664f)
            curveTo(4.484f, 10.68f, 5.711f, 10f, 8f, 10f)
            quadToRelative(0.39f, 0f, 0.74f, 0.025f)
            curveToRelative(0.226f, -0.341f, 0.496f, -0.65f, 0.804f, -0.918f)
            quadTo(8.844f, 9.002f, 8f, 9f)
            curveToRelative(-5f, 0f, -6f, 3f, -6f, 4f)
            reflectiveCurveToRelative(1f, 1f, 1f, 1f)
            close()
        }
    }.build()
}

val DoubleBack: ImageVector by lazy {
    ImageVector.Builder(
        name = "angle-double-left",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 448f,
        viewportHeight = 512f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(223.7f, 239f)
            lineToRelative(136f, -136f)
            curveToRelative(9.4f, -9.4f, 24.6f, -9.4f, 33.9f, 0f)
            lineToRelative(22.6f, 22.6f)
            curveToRelative(9.4f, 9.4f, 9.4f, 24.6f, 0f, 33.9f)
            lineTo(319.9f, 256f)
            lineToRelative(96.4f, 96.4f)
            curveToRelative(9.4f, 9.4f, 9.4f, 24.6f, 0f, 33.9f)
            lineTo(393.7f, 409f)
            curveToRelative(-9.4f, 9.4f, -24.6f, 9.4f, -33.9f, 0f)
            lineToRelative(-136f, -136f)
            curveToRelative(-9.5f, -9.4f, -9.5f, -24.6f, -0.1f, -34f)
            close()
            moveToRelative(-192f, 34f)
            lineToRelative(136f, 136f)
            curveToRelative(9.4f, 9.4f, 24.6f, 9.4f, 33.9f, 0f)
            lineToRelative(22.6f, -22.6f)
            curveToRelative(9.4f, -9.4f, 9.4f, -24.6f, 0f, -33.9f)
            lineTo(127.9f, 256f)
            lineToRelative(96.4f, -96.4f)
            curveToRelative(9.4f, -9.4f, 9.4f, -24.6f, 0f, -33.9f)
            lineTo(201.7f, 103f)
            curveToRelative(-9.4f, -9.4f, -24.6f, -9.4f, -33.9f, 0f)
            lineToRelative(-136f, 136f)
            curveToRelative(-9.5f, 9.4f, -9.5f, 24.6f, -0.1f, 34f)
            close()
        }
    }.build()
}

val DoubleForward: ImageVector by lazy {
    ImageVector.Builder(
        name = "angle-double-right",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 448f,
        viewportHeight = 512f
    ).apply {
        path(
            fill = SolidColor(Color.Black)
        ) {
            moveTo(224.3f, 273f)
            lineToRelative(-136f, 136f)
            curveToRelative(-9.4f, 9.4f, -24.6f, 9.4f, -33.9f, 0f)
            lineToRelative(-22.6f, -22.6f)
            curveToRelative(-9.4f, -9.4f, -9.4f, -24.6f, 0f, -33.9f)
            lineToRelative(96.4f, -96.4f)
            lineToRelative(-96.4f, -96.4f)
            curveToRelative(-9.4f, -9.4f, -9.4f, -24.6f, 0f, -33.9f)
            lineTo(54.3f, 103f)
            curveToRelative(9.4f, -9.4f, 24.6f, -9.4f, 33.9f, 0f)
            lineToRelative(136f, 136f)
            curveToRelative(9.5f, 9.4f, 9.5f, 24.6f, 0.1f, 34f)
            close()
            moveToRelative(192f, -34f)
            lineToRelative(-136f, -136f)
            curveToRelative(-9.4f, -9.4f, -24.6f, -9.4f, -33.9f, 0f)
            lineToRelative(-22.6f, 22.6f)
            curveToRelative(-9.4f, 9.4f, -9.4f, 24.6f, 0f, 33.9f)
            lineToRelative(96.4f, 96.4f)
            lineToRelative(-96.4f, 96.4f)
            curveToRelative(-9.4f, 9.4f, -9.4f, 24.6f, 0f, 33.9f)
            lineToRelative(22.6f, 22.6f)
            curveToRelative(9.4f, 9.4f, 24.6f, 9.4f, 33.9f, 0f)
            lineToRelative(136f, -136f)
            curveToRelative(9.4f, -9.2f, 9.4f, -24.4f, 0f, -33.8f)
            close()
        }
    }.build()
}