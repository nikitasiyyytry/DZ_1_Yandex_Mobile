package com.example.usersurvey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.usersurvey.ui.theme.UserSurveyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserSurveyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserSurveyScreen()
                }
            }
        }
    }
}

@Composable
fun UserSurveyScreen() {
    // Состояния для всех полей формы
    var name by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf(25f) }
    var selectedGender by rememberSaveable { mutableStateOf("male") }
    var isSubscribed by rememberSaveable { mutableStateOf(false) }
    var showResult by rememberSaveable { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Заголовок
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Аватар (если есть изображение в drawable)
        // Image(
        //     painter = painterResource(id = R.drawable.ic_avatar),
        //     contentDescription = "Avatar",
        //     modifier = Modifier.size(80.dp)
        // )

        Spacer(modifier = Modifier.height(8.dp))

        // 1. Поле ввода имени
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                showError = false
            },
            label = { Text(stringResource(R.string.name_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = showError,
            supportingText = {
                if (showError) {
                    Text(
                        text = stringResource(R.string.name_error),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            singleLine = true
        )

        // 2. Выбор возраста (Slider)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.age_label, age.toInt()),
                style = MaterialTheme.typography.bodyLarge
            )
            Slider(
                value = age,
                onValueChange = { age = it },
                valueRange = 1f..100f,
                steps = 98,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 3. Выбор пола (RadioButton)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.gender_label),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Мужской
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    RadioButton(
                        selected = selectedGender == "male",
                        onClick = { selectedGender = "male" }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.male))
                }

                // Женский
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    RadioButton(
                        selected = selectedGender == "female",
                        onClick = { selectedGender = "female" }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.female))
                }
            }
        }

        // 4. Подписка на рассылку (Checkbox)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSubscribed,
                onCheckedChange = { isSubscribed = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.subscribe_label))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 5. Кнопка отправить
        Button(
            onClick = {
                if (name.isBlank()) {
                    showError = true
                    showResult = false
                } else {
                    showError = false
                    showResult = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.submit_button))
        }

        // Отображение результата
        if (showResult) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.result_name, name),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(R.string.result_age, age.toInt()),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(
                            R.string.result_gender,
                            if (selectedGender == "male")
                                stringResource(R.string.male)
                            else
                                stringResource(R.string.female)
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = if (isSubscribed)
                            stringResource(R.string.result_subscribe_yes)
                        else
                            stringResource(R.string.result_subscribe_no),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

// Предпросмотр в светлой теме
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun UserSurveyPreviewLight() {
    UserSurveyTheme(darkTheme = false) {
        UserSurveyScreen()
    }
}

// Предпросмотр в тёмной теме
@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UserSurveyPreviewDark() {
    UserSurveyTheme(darkTheme = true) {
        UserSurveyScreen()
    }
}

// Предпросмотр в ландшафтной ориентации
@Preview(showBackground = true, name = "Landscape", widthDp = 640, heightDp = 360)
@Composable
fun UserSurveyPreviewLandscape() {
    UserSurveyTheme {
        UserSurveyScreen()
    }
}