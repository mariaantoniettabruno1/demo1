let ClienteContainer = {
    data: function () {
        return {
            showTabella: false,
            azione: "",
            tabella: [],
            headers: [],
            updater: 0,
            singleSelect: true,
            selected: []
        }
    },
    template: `
        <v-main>
            <v-container>
                <div>
                    <v-btn
                        color="secondary"
                        depressed
                        elevation="2"
                        outlined
                        plain
                        raised
                        v-on:click="showRipetizioni"> showRipetizioni
                    </v-btn>
                    <v-btn
                        color="secondary"
                        depressed
                        elevation="2"
                        outlined
                        plain
                        raised
                        v-on:click="showPrenotazione"> showPrenotazione
                    </v-btn>
                </div>
                <div v-if="showTabella">
                    <v-data-table
                        v-model="selected"
                        :headers="headers"
                        :items="tabella"
                        :single-select="singleSelect"
                        :items-per-page="5"
                        class="elevation-1"
                        item-key="nome"
                        show-select
                      ></v-data-table>
                </div>
                <div id="show">{{selected}}</div>
                </v-container>
        </v-main>
    `,
    methods: {
        async showRipetizioni() {
            this.azione = "showRipetizioni";
            await this.getHeaders();
        },

        async showPrenotazione() {
            this.azione = "showPrenotazione";
            await this.getHeaders();
            //this.$session.getAll();
        },

        async getHeaders() {
            let result = await fetch(`ModificaServlet?azione=${this.azione}`);
            this.tabella = await result.json();

            if (this.tabella.length > 0) {
                this.headers = [];
                for (let key in this.tabella[0]) {
                    this.headers.push({text: this.primaLetteraMaiuscola(key), value: key});
                }
            }
            this.showTabella = true;
        },

        primaLetteraMaiuscola(str) {
            return str.charAt(0).toUpperCase() + str.slice(1);
        }
    }
}