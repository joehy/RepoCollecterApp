
import android.app.Activity
import android.opengl.Visibility
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.repocollector.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Job

@Composable
fun LoginPage(paddingValues: PaddingValues, onGoogleSignInClick: () -> Job, onRegisterScreen: () -> Unit,onSingInScreen: () -> Unit) {
    var user by remember { mutableStateOf<FirebaseUser?>(null) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var passwordVisibility by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val currentErrorMessage by rememberUpdatedState(errorMessage)
    LaunchedEffect(currentErrorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Icon
        Image(
            painter = painterResource(id = R.drawable.github_icon),
            contentDescription = "App Icon",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Email & Password
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    errorMessage = "Email and Password cannot be empty"
                } else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onSingInScreen() // Navigate to the next screen on successful login
                            } else {
                                errorMessage = task.exception?.message ?: "Login failed"
                            }
                        }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Text(

            text = "Create new account",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 18.sp,  // Set font size
                fontWeight = FontWeight.Bold,  // Set font weight
                color = MaterialTheme.colorScheme.primary,  // Set color
            ),
            modifier = Modifier.padding(16.dp).clickable(onClick = {
                onRegisterScreen()
            })  // Add padding around the text
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp), // Padding for the row
            horizontalArrangement = Arrangement.SpaceEvenly, // Space images evenly
            verticalAlignment = Alignment.CenterVertically // Center align the images
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = "Google Login",
                modifier = Modifier
                    .size(48.dp) // Size of the image
                    .clickable { onGoogleSignInClick() }
            )

            FacebookLoginWithImage { accessToken ->
                firebaseAuthWithFacebook(accessToken) { firebaseUser ->
                    user = firebaseUser
                    onSingInScreen()
                }
            }

        }

    }

}



fun firebaseAuthWithFacebook(accessToken: AccessToken, onAuthComplete: (FirebaseUser?) -> Unit) {
    val credential = FacebookAuthProvider.getCredential(accessToken.token)
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                onAuthComplete
            } else {
                Log.e("FirebaseAuth", "Authentication failed: ${task.exception?.message}")
                onAuthComplete(null)
            }
        }
}
@Composable
fun FacebookLoginWithImage(onLoginSuccess: (AccessToken) -> Unit) {
    val context = LocalContext.current
    val callbackManager = CallbackManager.Factory.create()

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable {

                val loginManager = LoginManager.getInstance()
                loginManager.logInWithReadPermissions(context as Activity, listOf("email", "public_profile"))
                loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        onLoginSuccess(result.accessToken)
                    }

                    override fun onCancel() {
                        Log.d("FacebookLogin", "Login canceled")
                    }

                    override fun onError(error: FacebookException) {
                        Log.e("FacebookLogin", "Error: ${error.message}")
                    }
                })
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_facebook_logo), // Your Facebook logo resource
            contentDescription = "Facebook Login",
            modifier = Modifier.fillMaxSize() // Ensure the image fills the circle
        )
    }
}


