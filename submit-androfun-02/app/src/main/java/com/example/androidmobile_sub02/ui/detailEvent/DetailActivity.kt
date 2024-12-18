package com.example.androidmobile_sub02.ui.detailEvent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.androidmobile_sub02.R
import com.example.androidmobile_sub02.data.local.entity.EventEntity
import com.example.androidmobile_sub02.data.remote.response.DetailEventResource
import com.example.androidmobile_sub02.data.remote.response.DetailRecipeResource
import com.example.androidmobile_sub02.databinding.ActivityDetailBinding
import com.example.androidmobile_sub02.ui.ViewModelFactory
import com.example.androidmobile_sub02.ui.themeSetting.ThemeSettingViewModel
import com.example.androidmobile_sub02.utils.DateUtils
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailEventViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    private val viewModel: ThemeSettingViewModel by viewModels() {
        ViewModelFactory.getInstance(application)
    }

    private var isFavEvent = false
    private var idEvent: Int = 0
    private var dataDetail: EventEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.dicoding_color)
        supportActionBar?.title = "Detail Event"
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idEvent = intent.getIntExtra(ID_EVENT_KEY, 0)

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.getDetailEvent(idEvent)

        detailViewModel.detailEvent.observe(this) {
            setUpDetailEvent(it)
        }

        detailViewModel.isEventFav(eventId = idEvent).observe(this) {
            isFavEvent = it
//            setIconFavorite(it)
        }

//        binding.favoriteButton.setOnClickListener {
//            if (dataDetail != null) {
//                if (isFavEvent) {
//                    detailViewModel.deleteEventFav(idEvent)
//                    setIconFavorite(false)
//                } else {
//                    detailViewModel.insertEventFav(dataDetail!!)
//                    setIconFavorite(true)
//                }
//            } else {
//                Log.e("TAG", "dataDetail is null, cannot insert/delete favorite")
//            }
//        }

        lifecycleScope.launch {
            viewModel.getThemeSetting().collect{ isDarkModeActive ->
                setAppTheme(isDarkModeActive)
            }
        }
    }

    private fun setAppTheme(darkModeActive: Boolean) {
        if (darkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

//    private fun setIconFavorite(isFavorite: Boolean) {
//        binding.favoriteButton.setImageResource(
//            if (isFavorite) R.drawable.icon_love else R.drawable.icon_fav_outline
//        )
//    }

    private fun setUpDetailEvent(it: DetailRecipeResource) {
        val eventData = it.data
        if (eventData != null) {
            binding.tvNameEvent.text = eventData.title ?: "No Title"
            binding.tvSumarry.text = eventData.ingredients?.joinToString("\n") ?: "No Ingredients"
            binding.tvOwnerEvent.text = eventData.instructions?.joinToString("\n") ?: "No Instructions"
            Glide.with(this)
                .load(eventData.image)
                .centerCrop()
                .into(binding.ivCoverEvent)

            Glide.with(this)
                .load(eventData.image)
                .centerCrop()
                .into(binding.ivIcon)
        } else {
            Log.e("DetailActivity", "Event data is null")
        }


////        val beginTime = DateUtils.formatDate(it.event?.beginTime ?: "", targetFormat = "dd MMM yyyy HH:mm")
////        val endTime = DateUtils.formatDate(it.event?.endTime ?: "", targetFormat = "HH:mm")
////        val quotas = it.event?.quota?.minus(it.event?.registrants ?: 0) ?: 0
////        binding.tvEventDate.text = "$beginTime - $endTime"
//        binding.tvNameEvent.text = it.data?.firstOrNull()?.title ?: ""
//        binding.tvSumarry.text = it.data?.firstOrNull()?.ingredients?.joinToString("\n") ?: ""
//        binding.tvOwnerEvent.text = it.data?.firstOrNull()?.instructions?.joinToString("\n") ?: ""
////        binding.tvCategory.text = it.event?.category ?: ""
////        binding.tvQuotas.text = "$quotas Tersedia"
////        binding.tvDescription.text = Html.fromHtml(it.event?.description, Html.FROM_HTML_MODE_COMPACT)
////
//        Glide.with(this)
//            .load(it.data?.firstOrNull()?.image)
//            .centerCrop()
//            .into(binding.ivCoverEvent)
//
//        Glide.with(this)
//            .load(it.data?.firstOrNull()?.image)
//            .centerCrop()
//            .into(binding.ivIcon)

        val data = it

        binding.centerButton.setOnClickListener {
            val link = data.data?.source
            if (!link.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply {
                    setPackage("com.android.chrome")
                }
                startActivity(intent)
            }
        }
//
//        dataDetail = EventEntity(
//            id = it.event?.id ?: 0,
//            name = it.event?.name ?: "",
//            beginTime = it.event?.beginTime ?: "",
//            endTime = it.event?.endTime ?: "",
//            mediaCover = it.event?.mediaCover ?: "",
//            imageLogo = it.event?.imageLogo ?: "",
//            ownerName = it.event?.ownerName ?: "",
//            category = it.event?.category ?: "",
//            description = it.event?.description ?: "",
//            link = it.event?.link ?: "",
//            quota = it.event?.quota ?: 0,
//            registrants = it.event?.registrants ?: 0,
//            summary = it.event?.summary ?: "",
//            cityName = it.event?.cityName ?: ""
//        )

        Log.e("DetailData", "setUpDetailEvent: $dataDetail")
    }

    private fun showLoading(it: Boolean) {
        if (it) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.apply {
                progressBar.visibility = View.GONE
//                dateContainer.visibility = View.VISIBLE
                ivCoverEvent.visibility = View.VISIBLE
                ivIcon.visibility = View.VISIBLE
//                tvCategory.visibility = View.VISIBLE
                scrollViewDetail.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val ID_EVENT_KEY = "id_event_key"
    }
}
