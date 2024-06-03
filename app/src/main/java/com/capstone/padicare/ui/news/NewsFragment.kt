import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.padicare.R
import com.capstone.padicare.databinding.FragmentNewsBinding
import com.capstone.padicare.model.ViewModelFactory
import com.capstone.padicare.ui.news.NewsAdapter
import com.capstone.padicare.ui.news.NewsViewModel

class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels { ViewModelFactory.getInstance(requireContext())}
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the action bar
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        // Initialize RecyclerView and adapter
        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }

        // Fetch news data
        viewModel.fetchNews()

        // Observe the news data and update the UI
        viewModel.newsList.observe(viewLifecycleOwner) { newsList ->
            newsAdapter.submitList(newsList)
        }

        viewModel.getData().observe(viewLifecycleOwner) { user ->
            binding.tvHi.text = getString(R.string.hi, user.name)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
