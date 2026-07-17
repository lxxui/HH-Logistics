package com.example.logistics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// อิมพอร์ตหน้าจอของคุณฝ้ายที่อยู่ในโฟลเดอร์ ui เข้ามาทำงาน
import com.example.logistics.ui.PickingPrepScreen
import com.example.logistics.ui.PickingExecutionScreen

// ✅ ย้ายกลับมาประกาศไว้ที่นี่เพื่อให้ระบบรู้จัก AppScreen ทันที ไม่ขึ้น Error แดงแล้วครับ
enum class AppScreen {
    BranchSelection,   // หน้าเลือกสาขา
    PickingPrep,       // หน้าเตรียมพร้อมระบุพิกัด
    PickingExecution   // หน้าทำรายการจัดสินค้าจริง
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainAppNavigator()
                }
            }
        }
    }
}

@Composable
fun MainAppNavigator() {
    // 📊 State สำหรับควบคุมทิศทางการนำทาง
    var currentScreen by remember { mutableStateOf(AppScreen.BranchSelection) }
    var selectedBranchName by remember { mutableStateOf("") }

    // 📦 ถังเก็บข้อมูลชั่วคราวสำหรับส่งผ่านค่าไปยังหน้าถัดไป
    var currentJobId by remember { mutableStateOf("") }
    var currentZoneId by remember { mutableStateOf("") }
    var currentLocationCode by remember { mutableStateOf("") }
    var currentStack by remember { mutableStateOf(0) }
    var currentTray by remember { mutableStateOf(0) }
    var currentPiece by remember { mutableStateOf(0) }

    // 🔄 สลับหน้าจอตาม State ปัจจุบัน
    when (currentScreen) {
        AppScreen.BranchSelection -> {
            BranchListScreen(
                onBranchClick = { branchName ->
                    selectedBranchName = branchName
                    currentScreen = AppScreen.PickingPrep
                }
            )
        }

        AppScreen.PickingPrep -> {
            PickingPrepScreen(
                branchName = selectedBranchName,
                onStartPickingWithDetails = { jobId, zoneId, locationCode, stack, tray, piece ->
                    currentJobId = jobId
                    currentZoneId = zoneId
                    currentLocationCode = locationCode
                    currentStack = stack
                    currentTray = tray
                    currentPiece = piece

                    // สลับไปหน้าจอจัดสินค้าจริง
                    currentScreen = AppScreen.PickingExecution
                },
                onBack = {
                    currentScreen = AppScreen.BranchSelection
                }
            )
        }

        AppScreen.PickingExecution -> {
            // เรียกใช้ฟังก์ชันจริงจาก PickingExecutionScreen.kt ของคุณฝ้าย
            PickingExecutionScreen(
                jobId = currentJobId,
                zoneId = currentZoneId,
                onFinishJob = {
                    // เมื่อเสร็จงาน ให้เด้งกลับมาที่หน้าเตรียมจัดสินค้า
                    currentScreen = AppScreen.PickingPrep
                }
            )
        }
    }
}

// ==========================================
// 🏪 หน้าจอ BranchListScreen (มินิมอลสีเขียวเข้ม Enterprise)
// ==========================================
@Composable
fun BranchListScreen(onBranchClick: (String) -> Unit) {
    val darkGreen = Color(0xFF0F5132)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "เลือกสาขาเพื่อเริ่มงานจัดสินค้า",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212529)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onBranchClick("สาขาหลัก") },
            colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("🏪 เข้าสู่ระบบ: สาขาหลัก", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onBranchClick("สำนักงานใหญ่") },
            colors = ButtonDefaults.buttonColors(containerColor = darkGreen),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("🏢 เข้าสู่ระบบ: สำนักงานใหญ่", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}