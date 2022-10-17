package com.strayAlpaca.canvasclock.ui.page.faq

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.config.BaseActivity
import com.strayAlpaca.canvasclock.databinding.ActivityFaqBinding
import com.strayAlpaca.canvasclock.ui.recycler.adapter.FaqAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FaqActivity : BaseActivity<ActivityFaqBinding>(R.layout.activity_faq) {
    private val viewModel : FaqViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        setRecyclerView()
        setObserver()

        viewModel.getFaqList()
    }

    override fun setButton() {
        binding.ivbtnBack.setOnClickListener {
            finish()
        }
    }

    override fun setRecyclerView() {
        binding.rvFaq.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvFaq.adapter = FaqAdapter()
    }

    private fun setObserver() {
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.faqList.collect { faqList ->
                        (binding.rvFaq.adapter as FaqAdapter).setFaqList(faqList)
                    }
                }
            }
        }
    }
}