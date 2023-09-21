
let addProducerForm =
    `<div class="modalWrapper">
        <div class="contentWrapper">
            <div>
                <div class="text-end mb-4">
                    <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode.parentNode)" aria-label="Close"></button>
                </div>
                <div class="contentBody">
                    <div>
                        <div class="form-group">
                            <label for="name">Name</label>
                            <input id="inputName" class="form-control" type="text">
                        </div>
                        
                        <div class="form-group">
                            <label for="name">Description</label>
                            <textarea cols="4" id="inputDescription" class="form-control" type="text"></textarea>
                        </div>
                            <label for="name">Logotype</label>
                            <input type="file" id="logotype" class="form-control">
                        <div>
                        
                        <div class="form-group">
                            <label for="name">Website url</label>
                            <input id="websiteUrl" class="form-control" type="text">
                        </div>
                        </div>
                    </div>
        
                </div>
                <button class="btn btn-success mt-3" onclick="addProducer()">Add</button>
            </div>
        </div>
    </div>`

 async function addProducer() {
     let name = document.getElementById("inputName").value;
     let description = document.getElementById("inputDescription").value;
     let logotype = document.getElementById("logotype").files[0];
     let websiteUrl = document.getElementById("websiteUrl").value;

     let image = await getBase64(logotype).then((converted) => {
         return {
             type: "image/" + converted.split(';')[0].split('/')[1],
             base64Image: converted
         }
     })
     let body = {
         producer: {
             name: name,
             description: description,
             properties: Object.fromEntries(parsePropertiesAndReturnAsArray()),
             websiteUrl: websiteUrl
         },
         image: image
     }

     fetch("/producer/new", {
         method: "POST",
         body: JSON.stringify(body),
         headers: {
             "Content-Type": "application/json"
         }
     }).then((response) => {
         response.text().then((response) => {
             RequestService.processResponseAndDoRedirect(response, "http://localhost/good/manufacture-machine/view/all")
         })
     })
}
