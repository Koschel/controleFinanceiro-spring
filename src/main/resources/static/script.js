let idEdicao = null;

function carregarMovimentacoes(){

fetch("http://localhost:8080/movimentacoes")
    .then(response => {
        if(!response.ok){
            throw new Error("Erro ao carregar Movimentações.");
        }
        return response.json();
    }).then(data => {

        const tabela = document.getElementById("tabelaMovimentacoes");
        tabela.innerHTML = "";

        data.forEach(mov => {

            const tipoClasse = mov.tipo === "RECEITA" ? "receita" : "despesa";

            const tr = document.createElement("tr");
            tr.innerHTML = `<td>${mov.descricao}</td>
                            <td>${formatarMoeda(mov.valor)}</td>
                            <td class="${tipoClasse}">${mov.tipo}</td>
                            <td>
                            </td>
                            `;

            const tdAcoes = tr.children[3];


            /*Cria o botao de excluir*/
            const btnExcluir = document.createElement("button");
            btnExcluir.classList = ("btnExcluir");
            btnExcluir.textContent = "Excluir";
            btnExcluir.onclick = () => excluir(mov.id);

            /*Cria o botao de Editar*/
            const btnEditar = document.createElement("button")
            btnEditar.classList = ("btnEditar");
            btnEditar.textContent = "Editar";
            btnEditar.onclick = () =>editar(mov);

            tdAcoes.appendChild(btnEditar);
            tdAcoes.appendChild(btnExcluir);
            tabela.appendChild(tr);
        });
    }).catch(error =>{
    alert(error.message);
    });
}

function salvar(){


    const descricao = document.getElementById("descricao").value;
    const valor = document.getElementById("valor").value;
    const tipo = document.getElementById("tipo").value;

    const movimentacao = {
        descricao: descricao,
        valor: valor,
        tipo: tipo
    };

    if(idEdicao==null){
        fetch("http://localhost:8080/movimentacoes",{
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(movimentacao)
        }).then(async response => {
          if (!response.ok){
              const erros = await response.json();
              alert(erros.join("\n"));
              return;
          }
        }).then(()=> {
            limparFormulario();
            carregarMovimentacoes();
            carregarResumo();
        });
    }else{
        fetch(`/movimentacoes/${idEdicao}`, {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(movimentacao)
        }).then(() => {
            idEdicao = null;
            limparFormulario();
            carregarMovimentacoes();
            carregarResumo();
        })
    }
}

function editar(mov){
    document.getElementById("descricao").value = mov.descricao;
    document.getElementById("valor").value = mov.valor;
    document.getElementById("tipo").value = mov.tipo;

    idEdicao = mov.id;
}

function excluir(id){
    fetch(`/movimentacoes/${id}`, {
        method: "DELETE"
    }).then(() => {
        carregarMovimentacoes();
        carregarResumo();
    });
}

function limparFormulario(){
    document.getElementById("descricao").value = "";
    document.getElementById("valor").value = "";
    idEdicao = null;
}

function carregarResumo(){
    fetch("/movimentacoes/resumo")
        .then(response => {
            if(!response.ok){
                throw new Error("Erro ao carregar resumo.");
            }
            return response.json();
        }).then(data =>{
            atualizarCard("saldo", data.saldo);
            atualizarCard("receitas", data.receitas);
            atualizarCard("despesas", data.despesas);
        }).then(() => {
            carregarMovimentacoes();
        }).catch(error =>{
            alert(error.message);
        });
}

function formatarMoeda(valor){

    return new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    }).format(valor);
}

function atualizarCard(tipoResumo, valor){

    document.getElementById(tipoResumo).textContent = formatarMoeda(valor);
}
