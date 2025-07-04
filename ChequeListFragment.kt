package com.wahab.myapp




import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wahab.myapp.data.ChequeViewModel

class ChequeListFragment : Fragment() {

    private lateinit var viewModel: ChequeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_cancel_cheques, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView1)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this)[ChequeViewModel::class.java]
        viewModel.fetchCheques()


        viewModel.cheques.observe(viewLifecycleOwner) { cheques ->
            recyclerView.adapter = ChequeAdapter(cheques) { chequeId ->
                viewModel.cancelCheque(chequeId)
            }
        }
    }
}
