package com.example.weatherapp.ui.advancedscreen

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.DoubleArrow
import com.example.weatherapp.ui.theme.Grey40
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.example.weatherapp.ui.theme.Green40
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapp.ui.theme.Grey80
import com.example.weatherapp.viewmodal.AdvancedViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.modal.NewsItem
import com.example.weatherapp.ui.theme.LightBlue

@Composable
fun TrendingNewsHeader() {
    Row(
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Trending News",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Grey80,
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
fun CategoryList (selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("top", "sports", "politics", "technology", "health", "business", "science", "entertainment", "environment", "food")

    LazyRow( modifier = Modifier
        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                isSelected = category == selectedCategory,
                onClick = onCategorySelected
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: String,
    isSelected: Boolean,
    onClick: (String) -> Unit
) {
    val backgroundColor = if (isSelected) Green40 else Grey40
    val color = if (isSelected) Color.White else Color.Black
    val borderModifier = if (!isSelected) {
        Modifier.border(width = 2.dp, color = Green40, shape = RoundedCornerShape(20.dp))
    } else {
        Modifier
    }
    BasicText(
        text = category.replaceFirstChar { if(it.isLowerCase()) it.titlecase() else it.toString() },
        modifier = Modifier
            .padding(start = 8.dp, end = 2.dp, top = 8.dp, bottom = 8.dp)
            .then(borderModifier)
            .background(color = backgroundColor, shape = RoundedCornerShape(20.dp))
            .clickable { onClick(category) }
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp),
        style = MaterialTheme.typography.body1.copy(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = color,
        )
    )
}

@Composable
fun TrendingNewsList(newsItems: List<NewsItem>,  onNewsItemSelected: (NewsItem) -> Unit) {
    LazyRow(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
    ) {
        items(newsItems) { newsItem ->
            TrendingNewsItemBox(newsItem, onNewsItemSelected)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun TrendingNewsItemBox(newsItem: NewsItem, onItemClick: (NewsItem) -> Unit) {
    val painter = rememberAsyncImagePainter(newsItem.image_url)
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 16.dp)
            .width(300.dp)
            .heightIn(max = 350.dp, min = 350.dp)
            .shadow(
                elevation = 7.dp,  // Adjust this value to increase/decrease shadow size
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Black.copy(alpha = 0.4f),  // Lower alpha for less blur
                spotColor = Color.Black.copy(alpha = 0.4f)     // Lower alpha for less blur
            )
            .background(Color.White)
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
            text = newsItem.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            style = TextStyle(textAlign = TextAlign.Justify),
            lineHeight = 30.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        ClickableText(
            text = AnnotatedString("source"),
            style = TextStyle(
                fontSize = 25.sp,
                color = Green40,
                fontWeight = FontWeight.W600,
            ),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            onClick = {
                onItemClick(newsItem) // Gọi hàm callback khi nhấn vào
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
            text = newsItem.pubDate,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun WorldNewsHeader() {
    Row(
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Global Stories",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Grey80,
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
fun WorldNewsList(newsItems: List<NewsItem>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 78.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        newsItems.forEach { newsItem ->
            WorldNewsItemBox(newsItem)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun WorldNewsItemBox(newsItem: NewsItem) {
    val painter = rememberAsyncImagePainter(newsItem.image_url)
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .height(200.dp)
            .shadow(
                elevation = 7.dp,  // Adjust this value to increase/decrease shadow size
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.Black.copy(alpha = 0.4f),  // Lower alpha for less blur
                spotColor = Color.Black.copy(alpha = 0.4f)     // Lower alpha for less blur
            )
            .background(Color.White)
            .clickable { /* Handle click */ },
    verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .height(160.dp)
                .width(180.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
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
            modifier = Modifier.padding(end = 30.dp),
            text = newsItem.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 30.sp,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            text = newsItem.link,
            fontSize = 25.sp,
            color = Green40,
            fontWeight = FontWeight.W600,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = newsItem.pubDate,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun TrendingNewsDetail(newsItem: NewsItem, onBackClicked: () -> Unit) {
    val painter = rememberAsyncImagePainter(newsItem.image_url)
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Back",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = Grey80,
                            ),
                            modifier = Modifier.padding(top = 24.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(
                            modifier = Modifier
                                .padding(top = 24.dp, start = 24.dp)
                                .size(24.dp),
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "View All",
                            tint = Grey80,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Create and launch share intent
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, newsItem.link)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Grey80,
                            modifier = Modifier
                                .padding(top = 24.dp, end = 24.dp)
                                .size(24.dp)
                        )
                    }
                },
                backgroundColor = Grey40, // Sử dụng màu của surface cho top bar
                elevation = 0.dp // Bỏ phần border của top bar
            )
        },
        backgroundColor = Grey40,
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .height(230.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = newsItem.description,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = newsItem.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        style = TextStyle(textAlign = TextAlign.Justify),
                        lineHeight = 40.sp,
                    )

                    Text(
                        text = "Source",
                        style = TextStyle(
                            fontSize = 25.sp,
                            color = Green40,
                            fontWeight = FontWeight.W600,
                        ),
                        modifier = Modifier.padding(top = 24.dp),
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = newsItem.pubDate,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = newsItem.description,
                        fontSize = 20.sp,
                        style = TextStyle(textAlign = TextAlign.Justify),
                        lineHeight = 30.sp,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        ClickableText(
                            text = AnnotatedString("Read More"),
                            style = TextStyle(
                                fontSize = 23.sp,
                                color = LightBlue,
                                fontWeight = FontWeight.W600,
                            ),
                            modifier = Modifier.padding(start = 16.dp),
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.link))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAdvanced() {
    WeatherAppTheme {
        Advanced()
    }
}

@Composable
fun Advanced(viewModel: AdvancedViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var selectedCategory by remember { mutableStateOf("top") }
    val (selectedNewsItem, setSelectedNewsItem) = remember { mutableStateOf<NewsItem?>(null) }

    LaunchedEffect(selectedCategory) {
        viewModel.fetchNewsByCategory(selectedCategory)
    }

    DisposableEffect(Unit) {
        onDispose {
            println("cancel")
            viewModel.cancelJob()
        }
    }

    val newsItem by viewModel.newsItems.collectAsState()


    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(
                modifier = Modifier
                    .background(Grey40)
                    .fillMaxSize()
            ) {
                Column {
                    TrendingNewsHeader()
                    CategoryList(selectedCategory) {category ->
                        selectedCategory = category
                    }
                    TrendingNewsList(newsItem) { newsItem ->
                        setSelectedNewsItem(newsItem)
                    }
                }
            }
        }
        item {
            Box(
                modifier = Modifier
                    .background(Grey40)
                    .fillMaxSize()
            ) {
                LaunchedEffect(Unit) {
                    viewModel.fetchNewsWorld()
                }

                val newsWorldItem by viewModel.newsWorldItems.collectAsState()
                WorldNewsHeader()
                WorldNewsList(newsWorldItem)
            }
        }
    }

    selectedNewsItem?.let { newsItem ->
        TrendingNewsDetail(newsItem) {
            setSelectedNewsItem(null) // Đặt lại tin tức được chọn về null khi nhấn nút Back
        }
    }
}