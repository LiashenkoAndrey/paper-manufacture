let addImageForm =
    `<div class="modalWrapper">
        <div class="contentWrapper">
            <div>
                <div class="text-end mb-4">
                <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode.parentNode)" aria-label="Close"></button>
            </div>
            <div class="contentBody">
                  <div>
                   <label for="images">Add a new image</label>
                   <input id="inputImage" class="form-control" type="file">
                </div>
            </div>        
                <button class="btn btn-success mt-3" onclick="addImage()">Add</button>
            </div>
        </div>
    </div>`


async function enableImageDeleteForm(elem) {
    let id = elem.getAttribute("id");
    let form = `<div class="modalWrapper">
        <div class="contentWrapper">
            <div>
                <div class="text-end mb-4">
                <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode.parentNode)" aria-label="Close"></button>
            </div>
            <div class="contentBody">
                <h5 style="color: white">Are you sure that you want to delete this image?</h5>
            </div>        
                <button class="btn btn-success mt-3" onclick="deleteImageById(\'${id}\')">Delete</button>
            </div>
        </div>
    </div>`

    FormService.enable(form);
}


async function deleteImageById(id) {
    await fetch(`/good/manufacture-machine/image/${id}/delete`, {
        method: "DELETE"
    }).then((res) => {
        RequestService.processResponseAndReload(res)
    })
}


async function addImage() {
    let formData = new FormData();
    let img = document.getElementById("inputImage").files;
    formData.append("image", img[0]);
    formData.append("goodId", getQueryParam('id'))
    console.log(formData)
    await fetch("/good/manufacture-machine/image/new", {
        method: "POST",
        body: formData
    }).then((resp) => {
        RequestService.processResponseAndReload(resp)
    })
}