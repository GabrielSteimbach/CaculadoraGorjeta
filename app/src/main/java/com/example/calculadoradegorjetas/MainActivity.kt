package com.example.calculadoragorjeta

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadoragorjeta.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // CORRIGIDO: Sem espaço
    private lateinit var binding: ActivityMainBinding
    private var porcentagemGorjeta: Int = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // CORRIGIDO: Sem espaço
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sbPorcentagemGorjeta.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                porcentagemGorjeta = progress
                binding.tvPorcentagemAtual.text = getString(R.string.porcentagem_gorjeta_inicial).replace("15%",
                    "$porcentagemGorjeta%")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.btnCalcular.setOnClickListener {
            calcularGorjeta()
        }
    }

    private fun calcularGorjeta() {
        val stringCusto = binding.etCustoServico.text.toString()
        if (stringCusto.isEmpty()) {
            binding.tvResultadoGorjeta.text = ""
            esconderTeclado(binding.etCustoServico)
            return
        }

        val custo = stringCusto.toDoubleOrNull()
        if (custo == null) {
            // CORRIGIDO: Usando R.string
            binding.tvResultadoGorjeta.text = getString(R.string.valor_invalido)
            return
        }

        var gorjeta = custo * porcentagemGorjeta / 100

        if (binding.swArredondarGorjeta.isChecked) {
            // CORRIGIDO: Adicionado o "="
            gorjeta = kotlin.math.ceil(gorjeta)
        }

        // CORRIGIDO: Usando Locale para garantir R$
        val formatadorMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val gorjetaFormatada = formatadorMoeda.format(gorjeta)

        // CORRIGIDO: Usando o texto base do strings.xml
        val textoBase = getString(R.string.valor_gorjeta)
        binding.tvResultadoGorjeta.text = textoBase.replace("R$ 0,00", gorjetaFormatada)

        esconderTeclado(binding.etCustoServico)
    }

    // CORRIGIDO: Sem espaço
    private fun esconderTeclado(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // CORRIGIDO: Sem espaço
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}