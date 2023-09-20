let addVideoForm =
    `<div class="modalWrapper">
        <div class="contentWrapper">
            <div>
                <div class="text-end mb-4">
                <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode.parentNode)" aria-label="Close"></button>
            </div>
            <div class="contentBody">
                  <div>
                   <label for="images">Add new video</label>
                   <input id="inputVideo" class="form-control" type="file">
                </div>
            </div>        
                <button class="btn btn-success mt-3" onclick="addVideo()">Add</button>
            </div>
        </div>
    </div>`

async function addVideo() {
    let formData = new FormData();
    let video = document.getElementById("inputVideo").files;
    formData.append("video", video[0]);
    console.log(video.duration)
    formData.append("duration", "00:01:53")
    formData.append("goodId", getQueryParam('id'))
    console.log(formData)
    await fetch("/good/manufacture-machine/video/new", {
        method: "POST",
        body: formData
    }).then((resp) => {
        console.log(resp.status)
        RequestService.processResponse(resp)
    })
}