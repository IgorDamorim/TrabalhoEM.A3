# Estados possíveis
const estados = ["Start", "Enemy", "Treasure", "Trap", "Boss", "Exit"]

# Matriz de transição (probabilidades de ir para os próximos estados)
const P = [
    # Enemy  Treasure  Trap   Boss   Exit
    0.5    0.3       0.2     0.0    0.0;   # Start
    0.3    0.3       0.2     0.2    0.0;   # Enemy
    0.3    0.2       0.3     0.2    0.0;   # Treasure
    0.4    0.1       0.3     0.2    0.0;   # Trap
    0.0    0.0       0.0     0.0    1.0    # Boss (só vai para Exit)
]

# Função para escolher o próximo estado com base nas probabilidades
function escolher_proximo_estado(probs)
    r = rand()
    acumulado = 0.0
    for i in 1:length(probs)
        acumulado += probs[i]
        if r < acumulado
            return i
        end
    end
    return length(probs)  # fallback (último)
end

# Função principal de geração do mapa
function gerar_mapa_com_boss(tamanho_minimo::Int=5)
    mapa = ["Start"]
    estado_atual = 1  # Start
    passou_boss = false

    while length(mapa) < 30
        if estados[estado_atual] == "Exit"
            break
        end

        linha = estado_atual
        if linha > size(P, 1)
            break
        end

        proximo = escolher_proximo_estado(P[linha, :])
        estado_atual = proximo + 1  # porque colunas começam em Enemy (índice 2 em `estados`)

        nome_estado = estados[estado_atual]

        # Regra: só pode ir para Exit se passou pelo Boss
        if nome_estado == "Exit" && !passou_boss
            continue
        end

        if nome_estado == "Boss"
            passou_boss = true
        end

        push!(mapa, nome_estado)

        if nome_estado == "Exit" && length(mapa) >= tamanho_minimo
            break
        end
    end

    return mapa
end

# Função para rodar e imprimir
function main()
    mapa = gerar_mapa_com_boss(7)
    println("{")
    println("  \"mapa\": [", join(["\"$s\"" for s in mapa], ", "), "]")
    println("}")
end

main()
