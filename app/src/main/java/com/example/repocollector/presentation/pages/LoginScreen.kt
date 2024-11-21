
import android.opengl.Visibility
import android.widget.Toast
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.example.repocollector.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job

@Composable
fun LoginPage(paddingValues: PaddingValues, onGoogleSignInClick: () -> Job, onRegisterScreen: () -> Unit,onSingInScreen: () -> Unit) {
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
           /* Image(
                painter = painterResource(id = R.drawable.ic_facebook_logo),
                contentDescription = "Google Login",

                modifier = Modifier
                    .size(48.dp) // Size of the image
                    .clickable {  } // Clickable for interaction
            )*/

        }

    }
}
