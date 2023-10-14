
let sceneImage = document.getElementById("sceneImage")
let imgArr = sceneImage.getElementsByTagName("img");
let getImgEndpoint = '/upload/image/';

function switchImage(imgDiv) {
    let imgId = imgDiv.getAttribute("data-image_id");
    let imgUrl = getImgEndpoint + imgId;
    imgArr[0].src = imgUrl;
    imgArr[1].src = imgUrl;
    sceneImage.getElementsByTagName("button")[0].setAttribute("id", imgId)
}