package pe.com.dyd.app.pomodoro

import android.content.Context
import android.media.RingtoneManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Locale

private enum class Phase { WORK, SHORT, LONG }
private data class Prefs(val work: Int = 25, val short: Int = 5, val long: Int = 15, val sound: Boolean = true, val vibrate: Boolean = true, val tone: Int = 0, val dark: Boolean = true, val english: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PomodoroApp() }
    }
}

@Composable
private fun PomodoroApp() {
    val context = LocalContext.current
    val store = remember { context.getSharedPreferences("pomodoro", Context.MODE_PRIVATE) }
    fun readPrefs() = Prefs(store.getInt("work",25), store.getInt("short",5), store.getInt("long",15), store.getBoolean("sound",true), store.getBoolean("vibrate",true), store.getInt("tone",0), store.getBoolean("dark",true), store.getBoolean("english",false))
    var prefs by remember { mutableStateOf(readPrefs()) }
    var phase by remember { mutableStateOf(Phase.WORK) }
    var rounds by remember { mutableIntStateOf(store.getInt("rounds",0)) }
    var seconds by remember { mutableIntStateOf(store.getInt("remaining", prefs.work * 60).coerceAtLeast(0)) }
    var running by remember { mutableStateOf(false) }
    var settings by remember { mutableStateOf(false) }
    val english = prefs.english
    val dark = prefs.dark
    val bg = if (dark) androidx.compose.ui.graphics.Color(0xFF101114) else androidx.compose.ui.graphics.Color(0xFFF7F7FA)
    val fg = if (dark) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black
    val accent = if (dark) androidx.compose.ui.graphics.Color.White else androidx.compose.ui.graphics.Color.Black
    val phaseLabel = when (phase) { Phase.WORK -> if (english) "Work session" else "Sesión de trabajo"; Phase.SHORT -> if (english) "Short break" else "Descanso corto"; Phase.LONG -> if (english) "Long break" else "Descanso largo" }
    fun duration(p: Phase, config: Prefs = prefs) = when(p) { Phase.WORK -> config.work; Phase.SHORT -> config.short; Phase.LONG -> config.long }
    fun save() { store.edit().putInt("work",prefs.work).putInt("short",prefs.short).putInt("long",prefs.long).putBoolean("sound",prefs.sound).putBoolean("vibrate",prefs.vibrate).putInt("tone",prefs.tone).putBoolean("dark",prefs.dark).putBoolean("english",prefs.english).putInt("rounds",rounds).putInt("remaining",seconds).apply() }
    fun advance() {
        running = false
        if (phase == Phase.WORK) { rounds++; phase = if (rounds % 4 == 0) Phase.LONG else Phase.SHORT }
        else phase = Phase.WORK
        seconds = duration(phase) * 60
        save()
        if (prefs.sound) runCatching { val type = when (prefs.tone) { 1 -> RingtoneManager.TYPE_RINGTONE; 2 -> RingtoneManager.TYPE_ALARM; else -> RingtoneManager.TYPE_NOTIFICATION }; val uri = RingtoneManager.getDefaultUri(type); RingtoneManager.getRingtone(context, uri)?.play() }
        if (prefs.vibrate) runCatching { (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(350, VibrationEffect.DEFAULT_AMPLITUDE)) }
    }
    LaunchedEffect(running) { while (running) { delay(1000); if (seconds <= 1) advance() else { seconds--; save() } } }
    MaterialTheme(colorScheme = if (dark) darkColorScheme(primary = androidx.compose.ui.graphics.Color(0xFFD0BCFF), background = bg, surface = bg) else lightColorScheme(background = bg, surface = bg)) {
        Surface(
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            color = bg,
            contentColor = fg
        ) {
            if (settings) SettingsScreen(prefs, english, onBack = { settings = false }, onChange = { prefs = it; seconds = duration(phase, it) * 60; save() }, onReset = { prefs = Prefs(); phase = Phase.WORK; seconds = 1500; rounds = 0; running = false; save() })
            else Column(Modifier.fillMaxSize().padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(Modifier.fillMaxWidth().height(56.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { settings = true }) { Icon(Icons.Default.Menu, contentDescription = if (english) "Settings" else "Configuración") }
                    Spacer(Modifier.weight(1f))
                    Text(if (english) "POMODORO" else "POMODORO", style = MaterialTheme.typography.labelLarge, letterSpacing = 2.sp, color = fg.copy(alpha = .65f))
                    Spacer(Modifier.weight(1f)); Spacer(Modifier.size(48.dp))
                }
                Spacer(Modifier.weight(0.55f))
                Box(Modifier.fillMaxWidth().aspectRatio(1f), contentAlignment = Alignment.Center) {
                    val progress = 1f - seconds.toFloat() / (duration(phase).coerceAtLeast(1) * 60)
                    Canvas(Modifier.fillMaxSize().padding(12.dp)) {
                        val width = 20.dp.toPx(); drawArc(fg.copy(alpha=.14f), 135f, 270f, false, style = Stroke(width, cap=StrokeCap.Round))
                        drawArc(accent, 135f, 270f * progress, false, style = Stroke(width, cap=StrokeCap.Round))
                    }
                    Text(String.format(Locale.US, "%02d:%02d", seconds / 60, seconds % 60), fontSize = 58.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
                Spacer(Modifier.weight(.32f))
                Surface(shape = RoundedCornerShape(50), color = accent.copy(alpha=.15f)) { Text(phaseLabel.uppercase(), Modifier.padding(horizontal=16.dp, vertical=8.dp), color = accent, style = MaterialTheme.typography.labelMedium, letterSpacing=1.sp) }
                Spacer(Modifier.height(14.dp))
                Text(phaseLabel, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.weight(.45f))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(36.dp)) {
                    FilledIconButton(onClick = { running = !running }, modifier = Modifier.size(72.dp), shape = CircleShape) {
                        Canvas(Modifier.size(34.dp).semantics { contentDescription = if (running) "Pause" else "Start" }) {
                            if (running) {
                                drawRect(fg, Offset(size.width * .22f, size.height * .18f), androidx.compose.ui.geometry.Size(size.width * .2f, size.height * .64f))
                                drawRect(fg, Offset(size.width * .58f, size.height * .18f), androidx.compose.ui.geometry.Size(size.width * .2f, size.height * .64f))
                            } else {
                                val path = androidx.compose.ui.graphics.Path().apply { moveTo(size.width * .24f, size.height * .12f); lineTo(size.width * .82f, size.height * .5f); lineTo(size.width * .24f, size.height * .88f); close() }
                                drawPath(path, fg)
                            }
                        }
                    }
                    IconButton(onClick = { advance() }, modifier = Modifier.size(60.dp)) {
                        Canvas(Modifier.size(32.dp).then(Modifier.semantics { contentDescription = if (english) "Next timer" else "Siguiente contador" })) {
                            val path = androidx.compose.ui.graphics.Path().apply {
                                moveTo(size.width * .2f, size.height * .15f)
                                lineTo(size.width * .72f, size.height * .5f)
                                lineTo(size.width * .2f, size.height * .85f)
                                close()
                            }
                            drawPath(path, fg)
                            drawLine(fg, Offset(size.width * .83f, size.height * .15f), Offset(size.width * .83f, size.height * .85f), strokeWidth = 3.dp.toPx())
                        }
                    }
                }
                Spacer(Modifier.weight(.55f))
                Text(if (english) "${4 - rounds % 4} work sessions until long break" else "${4 - rounds % 4} sesiones de trabajo hasta el descanso largo", style = MaterialTheme.typography.bodyMedium, color = fg.copy(alpha=.72f), modifier = Modifier.padding(bottom = 24.dp))
            }
        }
    }
}

@Composable
private fun SettingsScreen(p: Prefs, english: Boolean, onBack: () -> Unit, onChange: (Prefs) -> Unit, onReset: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val labels = if (english) listOf("Work duration", "Short break", "Long break", "Sound when finished", "Vibrate when finished", "Notification sound", "Appearance", "Language", "Reset defaults") else listOf("Duración de trabajo", "Descanso corto", "Descanso largo", "Sonar al terminar", "Vibrar al terminar", "Sonido de notificación", "Tema", "Idioma", "Restablecer valores")
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal=20.dp)) {
        Row(verticalAlignment=Alignment.CenterVertically, modifier=Modifier.height(64.dp)) { IconButton(onClick=onBack) { Icon(Icons.Default.ArrowBack, contentDescription="Back") }; Text(if(english) "Settings" else "Configuración", style=MaterialTheme.typography.headlineSmall, fontWeight=FontWeight.SemiBold) }
        DurationSetting(labels[0],p.work){onChange(p.copy(work=it))}; DurationSetting(labels[1],p.short){onChange(p.copy(short=it))}; DurationSetting(labels[2],p.long){onChange(p.copy(long=it))}
        HorizontalDivider(Modifier.padding(vertical=8.dp))
        SettingSwitch(labels[3],p.sound){onChange(p.copy(sound=it))}; SettingSwitch(labels[4],p.vibrate){onChange(p.copy(vibrate=it))}
        Row(Modifier.fillMaxWidth().height(64.dp), verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text(labels[5]); Text(if(english) "${p.tone+1} of 3" else "${p.tone+1} de 3", style=MaterialTheme.typography.bodySmall, color=MaterialTheme.colorScheme.onSurfaceVariant) }
            Box { TextButton(onClick={expanded=true}) { Text(if(english) listOf("Default","Soft","Bright")[p.tone] else listOf("Predeterminado","Suave","Brillante")[p.tone]) }; DropdownMenu(expanded,{expanded=false}) { (0..2).forEach { i -> DropdownMenuItem(text={Text(if(english) listOf("Default","Soft","Bright")[i] else listOf("Predeterminado","Suave","Brillante")[i])},onClick={expanded=false;onChange(p.copy(tone=i))}) } } }
        }
        HorizontalDivider(Modifier.padding(vertical=8.dp))
        Row(Modifier.fillMaxWidth().height(64.dp), verticalAlignment=Alignment.CenterVertically) { Text(labels[6],Modifier.weight(1f)); SingleChoiceSegmentedButtonRow { SegmentedButton(!p.dark, onClick={onChange(p.copy(dark=false))}, shape=SegmentedButtonDefaults.itemShape(0, 2)){Text(if(english) "Light" else "Claro")}; SegmentedButton(p.dark, onClick={onChange(p.copy(dark=true))}, shape=SegmentedButtonDefaults.itemShape(1, 2)){Text(if(english) "Dark" else "Oscuro")} } }
        Row(Modifier.fillMaxWidth().height(64.dp), verticalAlignment=Alignment.CenterVertically) { Text(labels[7],Modifier.weight(1f)); SingleChoiceSegmentedButtonRow { SegmentedButton(!p.english, onClick={onChange(p.copy(english=false))}, shape=SegmentedButtonDefaults.itemShape(0, 2)){Text("ES")}; SegmentedButton(p.english, onClick={onChange(p.copy(english=true))}, shape=SegmentedButtonDefaults.itemShape(1, 2)){Text("EN")} } }
        Spacer(Modifier.height(20.dp)); OutlinedButton(onClick=onReset, modifier=Modifier.fillMaxWidth()) { Text(labels[8]) }
    }
}

@Composable private fun DurationSetting(label: String, value: Int, change: (Int)->Unit) {
    Row(Modifier.fillMaxWidth().height(72.dp), verticalAlignment=Alignment.CenterVertically) { Text(label,Modifier.weight(1f)); IconButton(onClick={if(value>1) change(value-1)}) { Text("−",fontSize=24.sp) }; Text("$value min",modifier=Modifier.widthIn(min=54.dp),style=MaterialTheme.typography.titleMedium); IconButton(onClick={if(value<180) change(value+1)}) { Text("+",fontSize=22.sp) } }
}
@Composable private fun SettingSwitch(label: String, checked: Boolean, change: (Boolean)->Unit) { Row(Modifier.fillMaxWidth().height(60.dp), verticalAlignment=Alignment.CenterVertically) { Text(label,Modifier.weight(1f)); Switch(checked,change) } }
