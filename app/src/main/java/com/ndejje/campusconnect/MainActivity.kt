package com.ndejje.campusconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ndejje.campusconnect.ui.theme.CampusConnectTheme

/**
 * Application entry point.
 *
 * Per coding standards this file is intentionally minimal:
 * it sets the content view and invokes the root Composable only.
 * All navigation, state management, and business logic reside in their
 * appropriate ViewModel, Repository, or Composable layers.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusConnectTheme {
                CampusConnectApp()
            }
        }
    }
}
