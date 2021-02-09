let AmministratoreContainer = {
    data: function () {
        return {
            showTabella: false, //non stampiamo la tabella vuota, quindi va a true solo se la fetch da risultato positivo
            singleSelect: true, //per poter selezionare solo un oggetto alla volta
            azione: "", // parametro che daremo alla fetch per i controlli della servlet
            headers: [], //header della tabella
            tabella: [], // item interni alla tabella
            selected: [], // oggetto selezionato dall'utente
            op: "", // operazione da passare al componente operazioni
        }
    },
    components: { OperazioniContainer },
    template: `
        <v-main>
            <v-container>
                <div>
                     <v-btn                   
                        color="#737CA1"
                        elevation="2"
                        
                        raised
                        v-on:click="showRipetizioni"> Ripetizioni
                    </v-btn>
                    <v-btn
                        color="#737CA1"
                        elevation="2"
                        
                        raised
                        v-on:click="showPrenotazione"> Cronologia Prenotazioni
                    </v-btn>
                       <v-btn
                        color="#737CA1"
                        elevation="2"
                        
                        raised
                        v-on:click="showDocente"> Lista Docenti
                    </v-btn>
                        <v-btn
                        color="#737CA1"
                        elevation="2"
                        
                        raised
                        v-on:click="showCorso"> Lista Corsi
                    </v-btn>             
                       <v-btn
                        color="#737CA1"
                        elevation="2"
                        
                        raised
                        v-on:click="showInsegna"> Lista Insegnamenti
                    </v-btn>
                    </div>
                <div v-if="showTabella" style="margin-top:10%">
                    <v-data-table
                        v-model="selected"
                        :headers="headers"
                        :items="tabella"
                        :single-select="singleSelect"
                        :items-per-page="5"
                        class="elevation-2"
                        item-key="id"
                        :sort-by="['id']"
                        show-select
                      ></v-data-table>
                </div>
                <operazioni-container :selected="selected" :operazione="op"></operazioni-container>
                </v-container>
        </v-main>
    `,
    mounted() {
        this.$root.$on('cambiata', async () => await this.getHeaders());
    },
    methods: {
        async showRipetizioni() {
            this.azione = "showRipetizioni";
            await this.getHeaders();
            this.op = "showRipetizioni";
        },

        async showPrenotazione() {
            this.azione = "showPrenotazioneAmministratore";
            await this.getHeaders();
            this.op = "showPrenotazione";
        },
        async showDocente() {
            this.azione = "showDocente";
            await this.getHeaders();
            this.op = "showDocente";
        },
        async showCorso() {
            this.azione = "showCorso";
            await this.getHeaders();
            this.op = "showCorso";
        },
        async showInsegna() {
            this.azione = "showInsegna";
            await this.getHeaders();
            this.op = "showInsegna";
        },

        async getHeaders() {
            let result = await fetch(`ModificaServlet?azione=${this.azione}&utente=${myStorage.getItem('utente')}`);
            this.tabella = await result.json();
            this.showTabella = true;
            if (this.tabella.length > 0) {
                this.headers = [];
                for (let key in this.tabella[0]) {
                    this.headers.push({text: this.primaLetteraMaiuscola(key), value: key});
                }
                this.headers[0] = { //rimpiazziamo id con # per bellezza
                    text: "#",
                    value: "id"
                };
            }
        },

        primaLetteraMaiuscola(str) {
            return str.charAt(0).toUpperCase() + str.slice(1);
        }
    }
}