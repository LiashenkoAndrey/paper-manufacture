let createManufactureMachineForm =
    `<div class="modalWrapper">
        <div class="modalBody">
            <div class="text-end mb-4">
                <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode)" aria-label="Close"></button>
            </div>
            <div style="display: flex; column-gap: 20px">
                <div style="min-width: 400px; margin-top: 20px">
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input id="inputName" class="form-control" type="text">
                    </div>
                    
                    <div class="form-group">
                        <label for="name">Description</label>
                        <textarea cols="4" id="inputDescription" class="form-control" type="text"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="name">Serial number</label>
                        <input id="serialNumber" class="form-control" type="text">
                    </div>                    
                    
                    <div>
                        <label for="images">Images</label>
                        <input id="inputFiles" class="form-control" type="file" multiple="multiple">
                    </div>
                    
                    <div>
                        <label for="producer">Producer</label>
                        <select name="producer" class="form-select" id="producersSelect"></select>
                    </div>
                </div>
    
                <div id="modalProperties">
                    <h5 class="mx-3 text-center">Properties</h5>
                    <div id="propertyContainer">
                        <div>
                            <input class="key" type="text">
                            <input class="val" type="text">
                        </div>
                        
                        <div>
                            <input class="key" type="text">
                            <input class="val" type="text">
                        </div>
                       
                        <div>
                            <input class="key" type="text">
                            <input class="val" type="text">
                        </div>
                        
                        <div>
                            <input class="key" type="text">
                            <input class="val" type="text">
                        </div>    
                        
                        <div>
                            <input class="key" type="text">
                            <input class="val" type="text">
                        </div>                                                                               
                    </div>
                    <span style="font-size: 54px" id="plusProperty" onclick="addProperty()">+</span>
                </div>
            </div>
            <button class="btn btn-success mt-3" onclick="createNewManufactureMachine()">Save</button>
        </div>
    </div>`

function getAndDisplayAllProducers() {
    let select = document.getElementById("producersSelect");
    fetch("/producer/all", {
        method: "GET"
    }).then((response) => {
        let contentType = response.headers.get("Content-Type");
        if (contentType !== "application/json") {
            throw Error("Unexpected content type: expected: 'application/json' but was '" + contentType + "'");
        }

        response.json().then((producers) => {
            for (let i = 0; i < producers.length; i++) {
                let option = document.createElement("option");
                let producer = producers[i];
                option.text = producer.name;
                option.value = producer.id;
                select.appendChild(option);
            }
        });
    })

}

let deleteManufactureMachineForm =
    `<div class="modalWrapper">
        <div class="modalBody">
            <div class="text-end mb-4">
                <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode)" aria-label="Close"></button>
            </div>
             <div>
                <h5 style="color: white">Are you sure that you want to delete this good?</h5>
                <button class="btn btn-success mt-3" onclick="deleteManufactureMachine()">Delete</button>
             </div>
        </div>
    </div>`

let newProperty =
    `<div>
        <input class="key" type="text">
        <input class="val" type="text">
     </div>`

function addProperty() {
    let container = document.getElementById('propertyContainer');
    container.insertAdjacentHTML('beforeend', newProperty);
}

function parsePropertiesAndReturnAsArray() {
    let keys = document.getElementsByClassName("key");
    let vals = document.getElementsByClassName("val");
    if (keys.length !== vals.length) {
        alert("keys amount differs from values");
        return;
    }
    let properties = new Map();
    for (let i = 0; i < keys.length; i++) {
        let key = keys[i].value;
        let val = vals[i].value;
        if (key !== '' && val !== '') {
            console.log(key+" , "+ val)
            properties.set(key, val);
        }
    }
    return properties;
}

async function deleteManufactureMachine() {
    console.log(new URLSearchParams(document.location.search).get("id"))
    fetch("/good/manufacture-machine/" + new URLSearchParams(document.location.search).get("id") + "/delete", {
        method: "DELETE",
    }).then((response) => {
        RequestService.processResponseAndDoRedirect(response, "http://localhost/good/manufacture-machine/view/all");
    })
}

async function createNewManufactureMachine() {
    let name = document.getElementById("inputName").value;
    let description = document.getElementById("inputDescription").value;
    let filesArray = document.getElementById("inputFiles").files;
    let producerId = document.getElementById("producersSelect").value;
    let serialNumber = document.getElementById("serialNumber").value;

    let images = [];
    for (let i = 0; i < filesArray.length; i++) {
         await getBase64(filesArray[i]).then((converted) => {
             const type = "image/" + converted.split(';')[0].split('/')[1];
             images.push({
                 type: type,
                 base64Image: converted
             })
        })
    }
    let body = {
        manufactureMachine: {
            name: name,
            description: description,
            properties: Object.fromEntries(parsePropertiesAndReturnAsArray()),
            serialNumber: serialNumber
        },
        images: images,
        producerId: producerId
    }

    fetch("/good/manufacture-machine/new", {
        method: "POST",
        body: JSON.stringify(body),
        headers: {
            "Content-Type": "application/json"
        }
    }).then((response) => {
        console.log(response.status)

        response.text().then((response) => {
            console.log(response)
            RequestService.processResponseAndDoRedirect(response, "http://localhost/good/manufacture-machine?id=" + parseInt(response))
        })
    })
}


async function getBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });
}



