var vid = document.getElementById("myVideo");

function myFunction() {
    alert(vid.duration);
}

(function localFileVideoPlayer() {
    'use strict'
    var URL = window.URL || window.webkitURL
    var displayMessage = function (message, isError) {
        var element = document.querySelector('#message')
        element.innerHTML = message
        element.className = isError ? 'error' : 'info'
    }
    var playSelectedFile = function (event) {
        var file = this.files[0]
        var type = file.type
        var videoNode = document.querySelector('video')
        var canPlay = videoNode.canPlayType(type)
        if (canPlay === '') canPlay = 'no'
        var message = 'Can play type "' + type + '": ' + canPlay
        var isError = canPlay === 'no'
        displayMessage(message, isError)

        if (isError) {
            return
        }

        var fileURL = URL.createObjectURL(file)
        videoNode.src = fileURL
    }
    var inputNode = document.querySelector('input')
    inputNode.addEventListener('change', playSelectedFile, false)
})()