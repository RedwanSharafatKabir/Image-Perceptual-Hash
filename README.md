## Image-Perceptual-Hash

### This library is built with the "Perceptual Hashing Algorithm." With this library, you can create 8-bit and 16-bit hashes of any image.
- - - -
#### Implementation

* <b>Step 1:</b> Add the JitPack repository to your build file. Add it in your root "/build.gradle":<br>

  ```
  
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
  
  ```
<br><br>
* <b>Step 2:</b> Add the dependency in root "/app/build.gradle":<br><br>
  ```
  
  dependencies {
          implementation 'com.github.RedwanSharafatKabir:Image-Perceptual-Hash:1.0.3'
  }
  
  ```
<br><br>
* <b>Step 3:</b> Dig in code:

  First, you need to create or get the <b>bitmap</b> of your desired image file.
  Then, pass the <b>bitmap</b> as a constructor argument and <b>bit size</b> as function arguments to <b>setData</b> function of <b>ImagePerceptualHash</b> class.
  
  * To create the 8-bit hash use the below code:<br><br>
    ```

    val imagePerceptualHash = ImagePerceptualHash(bitmap)
    imagePerceptualHash.setData(8)  // 8 is bit size
    
    ```
  <br>
  
  * To create the 16-bit hash use the below code:<br><br>
    ```
    
    val imagePerceptualHash = ImagePerceptualHash(bitmap)
    imagePerceptualHash.setData(16)  // 16 is bit size

    ```
  <br>
  
  * To get the hash result of the image use the below code (for both 8-bit and 16-bit hash):<br><br>
    ```

    imagePerceptualHash.getHash()
    val hashRsult = imagePerceptualHash.getHash()

    ```
