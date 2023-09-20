
class ImageSwitcher {
    constructor(className, idName) {
        this.idName = idName;
        this.currentImageIndex = 0;
        let array = document.getElementsByClassName(className);
        array[0].style.display = "block"
        this.images = array;
    }

    switchNext() {
        if ((this.currentImageIndex + 1) != this.images.length) {
            let old = this.images[this.currentImageIndex];
            old.removeAttribute("id");
            old.style.display = "none";

            let nextImg = this.images[this.currentImageIndex + 1];
            nextImg.setAttribute("id", this.idName);
            nextImg.style.display = "block"

            this.currentImageIndex++;
        }
    }

    switchBack() {
        if ((this.currentImageIndex - 1) != -1) {
            let old = this.images[this.currentImageIndex];
            old.removeAttribute("id");
            old.style.display = "none";

            let previousImg = this.images[this.currentImageIndex - 1];
            previousImg.setAttribute("id", this.idName);
            previousImg.style.display = "block"

            this.currentImageIndex--;
        }
    }

    setCurrentImageById(id) {
        if (this.currentImageIndex != id) {
            let old = this.images[this.currentImageIndex];
            old.removeAttribute("id");
            old.style.display = "none";

            let selected = this.images[id];
            selected.setAttribute("id", this.idName);
            selected.style.display = "block"

            this.currentImageIndex = id;
        }
    }
}

class NavImageSwitcher extends ImageSwitcher {
    constructor(className, idName) {
        super(className, idName);
    }

    setCurrentImageById(id) {
        if (this.currentImageIndex != id) {
            let old = this.images[this.currentImageIndex];
            old.removeAttribute("id");

            let selected = this.images[id];
            selected.setAttribute("id", this.idName);

            this.currentImageIndex = id;
        }
    }
}


let imageImageList = document.getElementsByClassName("imageListItem");
for(let i = 0; i < imageImageList.length; i++) {
    magnify(imageImageList[i], 2);
}

let mainImagesSwitcher = new ImageSwitcher("imageListItem", "currentMainImage");
mainImagesSwitcher.setCurrentImageById(0);

let navImagesSwitcher = new NavImageSwitcher("navImageListItem", "currentNavImage");
navImagesSwitcher.setCurrentImageById(0);

let next = document.getElementById("switchNextBtn");
let back = document.getElementById("switchBackBtn");

next.addEventListener("click", function() {
    mainImagesSwitcher.switchNext()
    navImagesSwitcher.setCurrentImageById(mainImagesSwitcher.currentImageIndex)
}, false);

back.addEventListener("click", function() {
    mainImagesSwitcher.switchBack();
    navImagesSwitcher.setCurrentImageById(mainImagesSwitcher.currentImageIndex)
}, false);


let navImages = document.getElementsByClassName("navImageListItem");

for (let i = 0; i < navImages.length; i++) {
    navImages[i].addEventListener("click", function() {
        mainImagesSwitcher.setCurrentImageById(i);
        navImagesSwitcher.setCurrentImageById(i);
    });
}
