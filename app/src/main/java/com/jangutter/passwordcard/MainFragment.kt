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
        val body =
            """
                <!DOCTYPE html>
                <html>
                <head>
                <title>Password Card</title>
                <style type="text/css">
                @font-face {
                    font-family: freefont_mono;
                    src: url("file:///android_asset/freefont_mono.ttf")
                }
                body {
                    font-family: freefont_mono;
                    font-size: 125%;
                }
                </style>
                <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0" />
                </head>
                <body>
                    <table style="margin-left: auto; margin-right: auto;">
                      <tr><td style="font-size: 75%"></td><td style="background-color: #ffffff">${strings[0]}</td></tr>
                      <tr><td style="font-size: 75%">1</td><td style="background-color: #ffffff">${strings[1]}</td></tr>
                      <tr><td style="font-size: 75%">2</td><td style="background-color: #c0c0c0">${strings[2]}</td></tr>
                      <tr><td style="font-size: 75%">3</td><td style="background-color: #ffc0c0">${strings[3]}</td></tr>
                      <tr><td style="font-size: 75%">4</td><td style="background-color: #c0ffc0">${strings[4]}</td></tr>
                      <tr><td style="font-size: 75%">5</td><td style="background-color: #ffffc0">${strings[5]}</td></tr>
                      <tr><td style="font-size: 75%">6</td><td style="background-color: #c0c0ff">${strings[6]}</td></tr>
                      <tr><td style="font-size: 75%">7</td><td style="background-color: #ffc0ff">${strings[7]}</td></tr>
                      <tr><td style="font-size: 75%">8</td><td style="background-color: #c0ffff">${strings[8]}</td></tr>
                    <tr><td colspan="2" style="font-size: 75%; text-align: center">$cardNumber</td></tr>
                    </table>
                </body>
                </html>
            """.trimIndent()
        binding.apply{
            webViewCard.loadDataWithBaseURL(null, body, "text/html", "UTF-8", null)
            webViewCard.settings.loadWithOverviewMode = true
            webViewCard.settings.useWideViewPort = true
        }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
