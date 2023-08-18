package com.perceptual.hash.utils

import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.BitmapCompat
import java.lang.Integer.parseInt
import java.nio.ByteBuffer
import java.util.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

object ImagePerceptualHash{

    private var blockWidth: Double ?= null
    private var blockHeight: Double ?= null
    private var weightTop: Double ?= null
    private var weightBottom: Double ?= null
    private var weightLeft: Double ?= null
    private var weightRight: Double ?= null
    private var blockTop: Double ?= null
    private var blockBottom: Double ?= null
    private var blockLeft: Double ?= null
    private var blockRight: Double ?= null
    private var yMod: Double ?= null
    private var yFrac: Double ?= null
    private var yInt: Double ?= null
    private var xMod: Double ?= null
    private var xFrac: Double ?= null
    private var xInt: Double ?= null

    private var bits = 0
    private var bitSize = 0
    private var data: MutableList<Int> ?= null
    private var wid: Int ?= null
    private var hei: Int ?= null
    private var evenX: Boolean = false
    private var evenY: Boolean = false

    private var result = ArrayList<Double>()
    private lateinit var blocks: Array<DoubleArray>
    private lateinit var finalResult: IntArray

    fun setData(imageBitmap: Bitmap, bits: Int){
        this.bits = bits

        when (bits) {
            8 -> bitSize = 64
            16 -> bitSize = 256
        }

        finalResult = IntArray(bitSize)
        blocks = Array(bits) { DoubleArray(bits) }

        createModifiedBitmap(imageBitmap)
    }

    private fun createModifiedBitmap(imageBitmap: Bitmap){
        val modifiedBitmap: Bitmap = Bitmap.createScaledBitmap(imageBitmap, 419, 207, false)
        wid = modifiedBitmap.width
        hei = modifiedBitmap.height

        Log.i("image_height_width_Log", "$wid $hei")

        val bytes = modifiedBitmap.byteCount
        val buffer = ByteBuffer.allocate(bytes)
        modifiedBitmap.copyPixelsToBuffer(buffer)
        val array: ByteArray = buffer.array()

        data = ArrayList()

        try {
            if (array != null) {
                for (element in array) {
                    data!!.add(Integer.valueOf(element.toInt()) and 0x00FF)
                }
            }

            Log.i("unsigned_array_Log", data.toString())

        } catch (e: Exception){
            Log.i("unsigned_array_error", e.message.toString())
        }

        Log.i("bitmap_byte_length", BitmapCompat.getAllocationByteCount(modifiedBitmap).toString())

        bmvBHash()
    }

    private fun bmvBHash(){
        evenX = (wid!! % bits) == 0
        evenY = (hei!! % bits) == 0

        if (evenX && evenY) {
            bmvBHashEven(data!!, bits)

        }
        else {
            for (i in 0 until bits) {
                for (j in 0 until bits) {
                    blocks[i][j] = 0.0
                }
            }

            blockWidth = wid!!.toDouble() / bits.toDouble()
            blockHeight = hei!!.toDouble() / bits.toDouble()

            (0 until hei!!).forEach { itY ->
                if (evenY) {
                    blockTop = floor(itY / blockHeight!!)
                    blockBottom = blockTop
                    weightTop = 1.0
                    weightBottom = 0.0

                } else {
                    yMod = (itY + 1) % blockHeight!!
                    yFrac = yMod!! - floor(yMod!!)
                    yInt = yMod!! - yFrac!!

                    weightTop = 1 - yFrac!!
                    weightBottom = yFrac!!

                    // yInt will be 0 on bottom/right borders and on block boundaries
                    if (yInt!! > 0.0 || itY + 1 == hei) {
                        blockTop = floor(itY / blockHeight!!)
                        blockBottom = blockTop

                    } else {
                        blockTop = floor(itY / blockHeight!!)
                        blockBottom = ceil(itY / blockHeight!!)
                    }

                }

                (0 until wid!!).forEach { itX ->
                    val ii = (itY * wid!! + itX) * 4
                    val alpha = data!![ii + 3]

                    val avgvalue = if(alpha == 0) 765 else data!![ii] + data!![ii + 1] + data!![ii + 2]

                    if (evenX) {
                        blockLeft = floor(itX / blockWidth!!)
                        blockRight = blockLeft
                        weightLeft = 1.0
                        weightRight = 0.0

                    } else {
                        xMod = (itX + 1) % blockWidth!!
                        xFrac = xMod!! - floor(xMod!!)
                        xInt = xMod!! - xFrac!!

                        weightLeft = 1 - xFrac!!
                        weightRight = xFrac!!

                        // xInt will be 0 on bottom/right borders and on block boundaries
                        if (xInt!! > 0 || itX + 1 == wid) {
                            blockLeft = floor(itX / blockWidth!!)
                            blockRight = blockLeft

                        } else {
                            blockLeft = floor(itX / blockWidth!!)
                            blockRight = ceil(itX / blockWidth!!)
                        }
                    }

                    blocks[blockTop!!.toInt()][blockLeft!!.toInt()] += avgvalue * weightTop!! * weightLeft!!
                    blocks[blockTop!!.toInt()][blockRight!!.toInt()] += avgvalue * weightTop!! * weightRight!!
                    blocks[blockBottom!!.toInt()][blockLeft!!.toInt()] += avgvalue * weightBottom!! * weightLeft!!
                    blocks[blockBottom!!.toInt()][blockRight!!.toInt()] += avgvalue * weightBottom!! * weightRight!!
                }
            }

            for (i in 0 until bits) {
                for (j in 0 until bits) {
                    result.add(blocks[i][j])
                }
            }

            translateBlocksToBits(result, blockWidth!! * blockHeight!!)
            getHash()
        }
    }

    private fun median(data: ArrayList<Double>): Double {
        val mdarr = data.take(data.size)

        Collections.sort(mdarr)

        if (mdarr.size % 2 == 0) {
            return (mdarr[mdarr.size / 2 - 1] + mdarr[mdarr.size / 2]) / 2.0
        }

        return mdarr[floor((mdarr.size / 2).toDouble()).toInt()]
    }

    private fun translateBlocksToBits(blocks: ArrayList<Double>, pixelsPerBlock: Double): IntArray {
        val halfBlockValue = (pixelsPerBlock * 256 * 3) / 2
        val bandsize = blocks.size / 4

        for (i in 0..3) {
            val num1 = i * bandsize
            val num2 = ((i + 1) * bandsize)-1

            val m = median(blocks.slice(num1..num2) as ArrayList<Double>)

            for(j in (i * bandsize) until ((i + 1) * bandsize)){
                val v = blocks[j]

                if((v > m) || (abs(v - m) < 1 && m > halfBlockValue)){
                    blocks[j] = 1.0

                } else {
                    blocks[j] = 0.0
                }
            }
        }

        for(temp in 0 until bitSize){
            finalResult[temp] = blocks[temp].toInt()
        }

        return finalResult
    }

    private fun bmvBHashEven(data: MutableList<Int>, bits: Int) {
        val blocksizeX = floor((wid!! / bits).toDouble())
        val blocksizeY = floor((hei!! / bits).toDouble())

        for (y in 0 until bits) {
            for (x in 0 until bits) {
                var total = 0.0

                for (iy in 0 until blocksizeY.toInt()) {
                    for (ix in 0 until blocksizeX.toInt()) {

                        val cx = x * blocksizeX.toInt() + ix
                        val cy = y * blocksizeY.toInt() + iy
                        val ii = (cy * wid!! + cx) * 4

                        val alpha = data[ii + 3]

                        total += if(alpha == 0) 765 else data[ii] + data[ii + 1] + data[ii + 2]
                    }
                }

                result.add(total)
            }
        }

        translateBlocksToBits(result, blocksizeX * blocksizeY)
        getHash()
    }

    fun getHash(): String {
        return bitsToHexHash(finalResult).toString()
    }

    private fun bitsToHexHash(bitsArray: IntArray): StringBuilder {
        val hex: StringBuilder = StringBuilder()

        for (i in bitsArray.indices step 4) {
            var nibble: String = bitsArray.slice(i..(i + 3)).toString()

            nibble = nibble.replace("[", "")
                .replace(", ", "").replace("]", "")

            val nibbleDec = parseInt(nibble,2)
            val nibbleHex = Integer.toHexString(nibbleDec)

            hex.append(nibbleHex)
        }

        return hex
    }
}
