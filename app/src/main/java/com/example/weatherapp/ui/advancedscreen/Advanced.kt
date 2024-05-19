package com.example.weatherapp.ui.advancedscreen

import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.DoubleArrow
import com.example.weatherapp.ui.theme.Grey40
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.example.weatherapp.ui.theme.Green40
import coil.compose.rememberAsyncImagePainter


@Composable
fun Advanced() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Grey40)
    ) {
        TrendingNewsHeader()
        CategoryList()
        NewsList()
    }
}

@Composable
fun TrendingNewsHeader() {
    Row(
        modifier = Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Trending News",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "View All",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Green40,
        )

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            imageVector = Icons.Default.DoubleArrow,
            contentDescription = "View All",
            tint = Green40,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CategoryList () {
    val categories = listOf("All", "Sports", "Politics", "Technology", "Health", "Business", "Science", "Entertainment", "Environment", "Food")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    LazyRow( modifier = Modifier
            .padding(top = 68.dp, start = 8.dp),
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                isSelected = category == selectedCategory,
                onClick = { selectedCategory = category }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Green40 else Grey40
    val color = if (isSelected) Color.White else Color.Black
    val borderModifier = if (!isSelected) {
        Modifier.border(width = 2.dp, color = Green40, shape = RoundedCornerShape(20.dp))
    } else {
        Modifier
    }
    BasicText(
        text = category,
        modifier = Modifier
            .padding(start = 8.dp, end = 2.dp, top = 8.dp, bottom = 8.dp)
            .then(borderModifier)
            .background(color = backgroundColor, shape = RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp),
        style = MaterialTheme.typography.body1.copy(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color,
        )
    )
}

@Composable
fun NewsList() {
    val newsItems = listOf(
        NewsItem("https://elacademy.edu.vn/wp-content/uploads/2024/05/Nhin-lai-nhan-sac-cua-Kim-Ji-Won-qua-tung.jpg", "Sự thăng hạng trong phong cách của Kim Ji Won là nhờ 3 \"bí kíp\"", "ScreenRant", "3 days a go"),
        NewsItem("https://newsmd2fr.keeng.vn/tiin/archive/imageslead/2024/05/16/3p5foojxpm06zsxnwyb61fhdvmk8mrbv.jpg", "Sự thăng hạng trong phong cách của Kim Ji Won là nhờ 3 \"bí kíp\"", "ScreenRant", "3 days a go"),
        NewsItem("https://newsmd2fr.keeng.vn/tiin/archive/imageslead/2024/05/16/3p5foojxpm06zsxnwyb61fhdvmk8mrbv.jpg", "Sự thăng hạng trong phong cách của Kim Ji Won là nhờ 3 \"bí kíp\"", "ScreenRant", "3 days a go"),
    )

    LazyRow(
        modifier = Modifier
            .padding(top = 140.dp, start = 8.dp),
    ) {
        items(newsItems) { newsItem ->
            NewsItemBox(newsItem)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun NewsItemBox(newsItem: NewsItem) {
    val painter = rememberAsyncImagePainter(newsItem.imageUrl)
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(300.dp)
            .height(380.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .clickable { /* Handle click */ }

    ) {
        Box(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
        ) {
            Image(
                painter = painter,
                contentDescription = newsItem.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            text = newsItem.description,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            style = TextStyle(textAlign = TextAlign.Justify),
            lineHeight = 30.sp,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            text = newsItem.source,
            fontSize = 25.sp,
            color = Green40,
            fontWeight = FontWeight.W600,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = newsItem.footer,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

data class NewsItem(val imageUrl: String, val description: String, val source: String, val footer: String, )

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAdvanced() {
    WeatherAppTheme {
        Advanced()
    }
}