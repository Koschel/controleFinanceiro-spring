let idEdicao = null;
let idExcluir = null;

let movimentacoes = [];
let filtroAtual = "TODOS";

let temporizadorAlerta;


function carregarMovimentacoes() {

    fetch("http://localhost:8080/movimentacoes")
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao carregar Movimentações.");
            }
            return response.json();
        }).then(data => {

        movimentacoes = data;
        renderizarTabela(movimentacoes)

    }).catch(error => {
        alert(error.message);
    });
}

function renderizarTabela(lista) {

    const tabela = document.getElementById("tabelaMovimentacoes");

    tabela.innerHTML = "";

    lista.forEach(mov => {

        const tipoClasse = mov.tipo === "RECEITA" ? "receita" : "despesa";

        const tr = document.createElement("tr");
        tr.innerHTML = `<td>${mov.descricao}</td>
                            <td>${formatarMoeda(mov.valor)}</td>
                            <td class="${tipoClasse}">${formartarTipo(mov.tipo)}</td>
                            <td>
                            </td>
                            `;

        const tdAcoes = tr.children[3];


        /*Cria o botao de excluir*/
        const btnExcluir = document.createElement("button");
        btnExcluir.classList.add("btnExcluir");
        btnExcluir.textContent = "Excluir";
        btnExcluir.onclick = () => excluir(mov);

        /*Cria o botao de Editar*/
        const btnEditar = document.createElement("button")
        btnEditar.classList.add("btnEditar");
        btnEditar.textContent = "Editar";
        btnEditar.onclick = () => editar(mov);

        tdAcoes.appendChild(btnEditar);
        tdAcoes.appendChild(btnExcluir);
        tabela.appendChild(tr);
    })
}

function selecionarFiltro(tipo) {
    filtroAtual = tipo;

    document.getElementById("btnTodos").classList.remove("ativo");
    document.getElementById("btnReceita").classList.remove("ativo");
    document.getElementById("btnDespesa").classList.remove("ativo");

    if (tipo === "TODOS") {
        document.getElementById("btnTodos").classList.add("ativo");
    }
    if (tipo === "RECEITA") {
        document.getElementById("btnReceita").classList.add("ativo");
    }
    if (tipo === "DESPESA") {
        document.getElementById("btnDespesa").classList.add("ativo");
    }

    aplicarFiltros();
}

function ordenarLista(lista, ordenacao) {

    const copia = [lista];

    switch (ordenacao) {
        case "DESCRICAO_ASC":
            return copia.sort((a, b) => a.descricao.localeCompare(b.descricao));
        case "DESCRICAO_DESC":
            return copia.sort((a, b) => b.descricao.localeCompare(a.descricao));
        case "VALOR_ASC":
            return copia.sort((a, b) => a.valor - b.valor);
        case "VALOR_DESC":
            return copia.sort((a, b) => b.valor - a.valor);
        default:
            return copia
    }
}

function aplicarFiltros() {
    const itemPesquisa = converteMinusculo(document.getElementById("pesquisa").value);
    const filtro = filtroAtual;
    const ordenacao = document.getElementById("ordenacao").value;

    /*Pesquisa*/
    const pesquisa = movimentacoes.filter(m => {

        const descricao = converteMinusculo(m.descricao);
        const tipo = converteMinusculo(m.tipo);

        return descricao.includes(itemPesquisa) ||
            tipo.includes(itemPesquisa);

    });

    /*Filtro*/
    let resultado = pesquisa.filter(p => {
        return filtro === "TODOS" || p.tipo === filtro;
    });

    resultado = ordenarLista(resultado, ordenacao)

    renderizarTabela(resultado);
}


function salvar() {

    const descricao = document.getElementById("descricao").value;
    const valor = document.getElementById("valor").value;
    const tipo = document.getElementById("tipo").value;


    const movimentacao = {
        descricao: descricao,
        valor: valor,
        tipo: tipo
    };

    if (idEdicao == null) {
        fetch("http://localhost:8080/movimentacoes", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(movimentacao)
        }).then(async response => {
            if (!response.ok) {
                const erros = await response.json();
                criarAlerta(erros.join("\n"), "erro");
            } else {
                criarAlerta("Salvo com sucesso", "sucesso");
            }
        }).then(() => {
            limparFormulario();
            carregarMovimentacoes();
            carregarResumo();
        });
    } else {
        fetch(`/movimentacoes/${idEdicao}`, {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(movimentacao)
        }).then(() => {
            movimentacaoAtualizada();
            limparFormulario();
            carregarMovimentacoes();
            carregarResumo();
        })
    }
}

function editar(mov) {
    document.getElementById("descricao").value = mov.descricao;
    document.getElementById("valor").value = mov.valor;
    document.getElementById("tipo").value = mov.tipo;

    modoEdicao(mov);

    idEdicao = mov.id;
}

function movimentacaoAtualizada() {
    idEdicao = null;
    criarAlerta("Edição realizada com com sucesso", "sucesso");
    modoCadastro();
}

function cancelaAtualizacao() {
    criarAlerta("Edição cancelada!", "aviso");
    limparFormulario();
    modoCadastro();
    idEdicao = null;
}

function modoCadastro() {
    document.getElementById("btn-atualizar").style.display = "none";
    document.getElementById("btn-salvar").style.display = "flex";
    document.getElementById("modo-editando").style.display = "none";
}

function modoEdicao(mov) {
    document.getElementById("btn-atualizar").style.display = "flex";
    document.getElementById("btn-salvar").style.display = "none";

    const editando = document.getElementById("modo-editando");
    editando.innerHTML = `<h3>Editando: <strong>${mov.descricao}</strong></h3>`;
    editando.style.display = "flex";

}

function excluir(mov) {

    idExcluir = mov.id;

    const movExcluir = document.getElementById("modalDescricao");

    movExcluir.innerHTML = `<strong>Descrição:</strong>
                            <p>${mov.descricao}</p>
                            <strong>Valor:</strong>
                            <p>${formatarMoeda(mov.valor)}</p>`;

    document.getElementById("modalExcluir").style.display = "flex";

}

function fecharModal() {
    document.getElementById("modalExcluir").style.display = "none";
    idExcluir = null;
}


function confirmaExclusao() {
    fetch(`/movimentacoes/${idExcluir}`, {
        method: "DELETE"
    }).then(() => {
        fecharModal()
        carregarMovimentacoes();
        carregarResumo();
        criarAlerta("Movimentação excluída com Sucesso!", "sucesso");
    });
}

function limparFormulario() {
    document.getElementById("descricao").value = "";
    document.getElementById("valor").value = "";
    idEdicao = null;
}

function carregarResumo() {
    fetch("/movimentacoes/resumo")
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao carregar resumo.");
            }
            return response.json();
        }).then(data => {
        atualizarCard("saldo", data.saldo);
        atualizarCard("receitas", data.receitas);
        atualizarCard("despesas", data.despesas);
    }).then(() => {
        carregarMovimentacoes();
    }).catch(error => {
        alert(error.message);
    });
}

function formatarMoeda(valor) {

    return new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    }).format(valor);
}

function atualizarCard(tipoResumo, valor) {
    document.getElementById(tipoResumo).textContent = formatarMoeda(valor);
}

function formartarTipo(tipo) {
    return tipo.charAt(0).toUpperCase() +
        tipo.slice(1).toLowerCase();
}

function criarAlerta(mensagem, tipo) {
    const alerta = document.getElementById("mensagem");

    clearTimeout(temporizadorAlerta);

    alerta.classList.remove("sucesso", "erro", "aviso");
    alerta.classList.add(tipo);
    alerta.innerHTML = `<h3>${mensagem}</h3>`;
    setTimeout(() => {
        alerta.innerHTML = "";

    }, 3000);
}

function converteMinusculo(texto) {
    return texto.toLowerCase();
}
