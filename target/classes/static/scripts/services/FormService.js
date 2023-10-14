
class FormService {

    static wrapper = document.getElementById("mainWrapper")

    static enable(form) {
        document.querySelector("body").style.overflowY = 'hidden';
        this.wrapper.style.filter = 'blur(8px)';
        document.body.insertAdjacentHTML('beforeend', form);
    }

    static enableAndExecuteFunction(form, ...functionNames) {
        this.enable(form);
        for (let i = 0; i < functionNames.length; i++) {
            functionNames[i]();
        }
    }

    static disable(form) {
        document.querySelector("body").style.overflowY = 'initial'
        form.remove();
        this.wrapper.style.filter = 'none';
    }
}