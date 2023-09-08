
class FormService {

    static wrapper = document.getElementById("mainWrapper")

    static enable(form) {
        document.querySelector("body").style.overflowY = 'hidden';
        this.wrapper.style.filter = 'blur(8px)';
        document.body.insertAdjacentHTML('beforeend', form);
    }

    static disable(form) {
        document.querySelector("body").style.overflowY = 'initial'
        form.remove();
        this.wrapper.style.filter = 'none';
    }
}


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
                        <input id="name" class="form-control" type="text">
                    </div>
                    
                    <div class="form-group">
                        <label for="name">Description</label>
                        <textarea cols="4" id="name" class="form-control" type="text"></textarea>
                    </div>
                        <label for="name">Images</label>
                        <input type="file" id="name" class="form-control" multiple="multiple">
                    <div>
                    
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
    let properties = [];
    for (let i = 0; i < keys.length; i++) {
        let key = keys[i].value;
        let val = vals[i].value;
        if (key !== '' && val !== '') {
            let prop = {};
            prop[key] = val;
            console.log(prop)
            properties.push(prop);
        }
    }
    return JSON.stringify(properties);
}

function createNewManufactureMachine() {

}