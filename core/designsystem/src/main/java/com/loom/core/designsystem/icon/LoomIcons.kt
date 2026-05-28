package com.loom.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Email
import androidx.compose.runtime.Composable


import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.loom.core.designsystem.R

/**
 * Loom icons. Material icons are [ImageVector]s.
 */
object LoomIcons {
    // Home
    val Home = Icons.Rounded.Home
    val HomeBorder = Icons.Outlined.Home

    // Search
    val Search = Icons.Rounded.Search
    val SearchBorder = Icons.Outlined.Search

    // Events
    val Events = Icons.Rounded.Groups
    val EventsBorder = Icons.Rounded.Groups

    // Profile
    val Person = Icons.Rounded.Person
    val PersonBorder = Icons.Outlined.Person

    // Notifications
    val Notifications = Icons.Rounded.Notifications
    val NotificationsBorder = Icons.Outlined.Notifications

    // Activity
    val Activity = Icons.Rounded.Bolt
    val ActivityBorder = Icons.Outlined.Bolt

    // Like
    //val Like = Icons.Rounded.Favorite
    //val LikeBorder = Icons.Outlined.FavoriteBorder

    // Repost
    val Repost = Icons.Rounded.Repeat
    val RepostBorder = Icons.Outlined.Repeat

    // Comment
    //val Comment = Icons.Rounded.ChatBubble
    //val CommentBorder = Icons.Outlined.ChatBubbleOutline

    // Interaction
    val Interaction = Icons.Rounded.Insights
    val InteractionBorder = Icons.Outlined.Insights

    // Next
    val ArrowBack = Icons.AutoMirrored.Rounded.ArrowBack

    val Email = Icons.Rounded.Email
    val EmailBorder = Icons.Outlined.Email

    val Palette = Icons.Rounded.ColorLens
    val PaletteBorder = Icons.Outlined.ColorLens

    // Dentro de tu object LoomIcons

    val Settings: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.icon_settings)

    val Like: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.icons_rounded_heart)
    val LikeBorder: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.icons_outlined_heart)

    val Comment: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.icons_rounded_comment)
    val CommentBorder: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.icons_outlined_comment)

}
