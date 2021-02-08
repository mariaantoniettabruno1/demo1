//component della pagina di landing.
let  HomeContainer = {
    data: function () {
        return {
            ruolo: "",
            welcomeMessage: "Benvenut*",
        }
    },
    template: `
        <v-main>
            <v-container>
                <h1>{{welcomeMessage}}</h1>
                <div class="text-center">
                    <v-btn
                        rounded
                        color="primary"
                        dark
                        to="/login"
                    >
                    Login
                    </v-btn>
                 </div> 
            </v-container>
        </v-main>
    `,

}