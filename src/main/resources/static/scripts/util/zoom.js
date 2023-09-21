function magnify(img, zoom) {
    var glass, w, h, bw;

    /* Create magnifier glass: */
    glass = document.createElement("DIV");
    glass.setAttribute("class", "img-magnifier-glass");

    /* Insert magnifier glass: */
    img.parentElement.insertBefore(glass, img);

    /* Set background properties for the magnifier glass: */
    glass.style.backgroundImage = "url('" + img.src + "')";
    glass.style.backgroundColor = "black";
    glass.style.backgroundRepeat = "no-repeat";
    glass.style.cursor = "none"
    glass.style.backgroundSize = ((img.width - 10) * zoom) + "px " + ((img.height - 10) * zoom) + "px";
    bw = 3;
    w = glass.offsetWidth / 2;
    h = glass.offsetHeight / 2;
    glass.style.display = 'none';

    let isEnabled = false;
    img.style.cursor ="zoom-in"

    function changeState() {
        if (isEnabled) {
            isEnabled = false;

            glass.removeEventListener("mousemove", moveMagnifier);
            img.removeEventListener("mousemove", moveMagnifier);
            glass.removeEventListener("touchmove", moveMagnifier);
            img.removeEventListener("touchmove", moveMagnifier);
            glass.style.display = 'none';
            glass.style.cursor = "zoom-in"
        } else {
            isEnabled = true;
            glass.addEventListener("mousemove", moveMagnifier);
            img.addEventListener("mousemove", moveMagnifier);
            glass.addEventListener("touchmove", moveMagnifier);
            img.addEventListener("touchmove", moveMagnifier);
            glass.style.cursor = "none"
            glass.style.display = 'block';
        }
    }

    img.addEventListener("click", changeState);
    glass.addEventListener("click", changeState);

    function moveMagnifier(e) {
      var pos, x, y;
      /* Prevent any other actions that may occur when moving over the image */
      e.preventDefault();
      /* Get the cursor's x and y positions: */
      pos = getCursorPos(e);
      x = pos.x;
      y = pos.y;
      /* Prevent the magnifier glass from being positioned outside the image: */
      if (x > img.width - (w / zoom)) {x = img.width - (w / zoom);}
      if (x < w / zoom) {x = w / zoom;}
      if (y > img.height - (h / zoom)) {y = img.height - (h / zoom);}
      if (y < h / zoom) {y = h / zoom;}
      /* Set the position of the magnifier glass: */
      glass.style.left = ((x - w)-100) + "px";
      glass.style.top = ((y - h)-100) + "px";
      /* Display what the magnifier glass "sees": */
      glass.style.backgroundPosition = "-" + ((x * zoom) - w + bw) + "px -" + ((y * zoom) - h + bw) + "px";
    }

    function getCursorPos(e) {
      var a, x = 0, y = 0;
      e = e || window.event;
      /* Get the x and y positions of the image: */
      a = img.getBoundingClientRect();
      /* Calculate the cursor's x and y coordinates, relative to the image: */
      x = e.pageX - a.left;
      y = e.pageY - a.top;
      /* Consider any page scrolling: */
      x = x - window.pageXOffset;
      y = y - window.pageYOffset;
      return {x : x, y : y};
    }
  }

  
