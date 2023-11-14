package com.jangutter.passwordcard

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jangutter.passwordcard.databinding.FragmentMainBinding
import org.pepsoft.passwordcard.PasswordCard

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        val appContext = requireContext().applicationContext
        val prefs = appContext.applicationContext.getSharedPreferences(
            appContext.packageName + "_preferences",
            Context.MODE_PRIVATE)

        val cardNumber = prefs.getString("card_number", "0")
        val cardID = cardNumber?.toLong(radix = 16) ?: 0
        val digitsOnlyArea = prefs.getBoolean("digits_only_area", false)
        val extraSymbols = prefs.getBoolean("extra_symbols", false)

        var passwordCard = PasswordCard(cardID, digitsOnlyArea, extraSymbols)
        val grid = passwordCard.getGrid()
        val strings = grid.map{String(it)}
        val lines = strings.mapIndexed{idx, value -> "${if (idx == 0) ' ' else idx} $value"}

        binding.apply{
            textviewFirst.text = lines.joinToString(separator = "\n")
        }

        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
