
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