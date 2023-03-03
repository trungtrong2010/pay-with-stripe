var stripe = Stripe('pk_test_51LPz3IDvtNyXog4yYOJSVB6TuTT8nmHv9zl8lL6tpBbyceG7mM5Pt4XlCpJ2a21DEotUeyCuqL8D9C3KyKkMAXh000dgbvn7GY');

var elements = stripe.elements();
var cardElement = elements.create('card');
cardElement.mount('#card-element');var cardholderName = document.getElementById('cardholder-name');
var setupForm = document.getElementById('setup-form');
var clientSecret = setupForm.dataset.secret;

setupForm.addEventListener('submit', function(ev) {
    ev.preventDefault();
    stripe.confirmCardSetup(
        clientSecret,
        {
            payment_method: {
                card: cardElement,
                billing_details: {
                    name: cardholderName.value,
                },
            },
        }
    ).then(function(result) {
        if (result.error) {
            // Display error.message in your UI.
            console.log(result.error)
        } else {
            console.log("thành công")

            // The setup has succeeded. Display a success message.
        }
    });
});
