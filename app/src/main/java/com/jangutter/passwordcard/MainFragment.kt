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

        val appContext = requireContext().applicationContext
        var prefs = appContext.applicationContext.getSharedPreferences(
            appContext.packageName + "_preferences",
            Context.MODE_PRIVATE)

        var cardNumber = prefs.getString("card_number", "0")
        var cardID = cardNumber?.toLong(radix = 16) ?: 0
        var digitsOnlyArea = prefs.getBoolean("digits_only_area", false)
        var extraSymbols = prefs.getBoolean("extra_symbols", false)

        var passwordCard = PasswordCard(cardID, digitsOnlyArea, extraSymbols)
        var grid = passwordCard.getGrid()
        var strings = grid.map{String(it)}
        var lines = strings.mapIndexed{idx, value -> "$idx $value"}

        binding.apply{
            textviewFirst.text = lines.joinToString(separator = "\n")
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
