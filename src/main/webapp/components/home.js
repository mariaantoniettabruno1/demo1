//component della pagina di landing.
let HomeContainer = {
    data: function () {
        return {
            ruolo: "",
            welcomeMessage: "Benvenut*",
            oggetto: [
                {id: "asjai", nome: "Gino", cognome: "Bruno"},
                {id: "asja2", nome: "Ciccio", cognome: "Pasticcio"},
                {id: "ajkja3", nome: "Cosmino", cognome: "Bobino"}
            ],
            headers: [
                {text: "ID", value: "id"},
                {text: "Nome", value: "nome"},
                {text: "Cognome", value: "cognome"},
            ],
            // headersRipetizioni : [
            //     {text: "Nome", value:"nome"},
            //     {text: "Cognome", value:"cognome"},
            //     {text: "Materia", value:"materia"},
            //     {text: "Data", value:"data"},
            //     {text: "Ora", value:"ora"}
            // ],
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
                 
                 <v-data-table
                    :headers="headers"
                    :items="oggetto"
                    :items-per-page="5"
                    class="elevation-1"
                  ></v-data-table>
                 
                 
            </v-container>
        </v-main>
    `,

}