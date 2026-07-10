package dev.gustavosa.inclusaodigital.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Catálogo de materiais educativos. Conteúdo fixo, curado pela equipe do
 * projeto — por isso não é uma entidade de banco (ver docs/02-arquitetura.md).
 */
public enum MaterialTopic {

    GOLPES("golpes", "O que são golpes?", List.of(
            "Golpistas costumam criar situações de urgência ou medo para fazer você agir sem pensar.",
            "Eles podem se passar por bancos, familiares, ou até órgãos do governo.",
            "Nunca compartilhe senhas, códigos de verificação ou dados bancários por telefone, mensagem ou e-mail.",
            "Na dúvida, desligue e ligue de volta para o número oficial da empresa, ou converse pessoalmente com um familiar de confiança."
    )),
    LINKS_FALSOS("links-falsos", "Links falsos", List.of(
            "Links falsos imitam sites conhecidos, como bancos, lojas e redes sociais, para roubar seus dados.",
            "Preste atenção a erros de português, endereços estranhos e promessas de prêmios ou ofertas muito boas.",
            "Não clique em links recebidos de números ou remetentes desconhecidos.",
            "Se um link parecer suspeito, não clique — pergunte a alguém de confiança antes."
    )),
    PIX("pix", "PIX seguro", List.of(
            "Nunca compartilhe sua chave PIX, senha ou código de confirmação com estranhos.",
            "Golpistas às vezes fingem ter feito um PIX por engano para pedir de volta um valor maior.",
            "Sempre confira o nome de quem vai receber antes de confirmar uma transferência.",
            "Use apenas o aplicativo oficial do seu banco para fazer PIX."
    )),
    WHATSAPP("whatsapp", "WhatsApp", List.of(
            "Golpistas podem clonar o número de um familiar e pedir dinheiro com urgência.",
            "Antes de enviar qualquer valor, ligue para a pessoa, com a voz dela, para confirmar o pedido.",
            "Tenha cuidado com links enviados em grupos, mesmo por pessoas conhecidas.",
            "Ative a verificação em duas etapas do WhatsApp para proteger sua conta."
    )),
    SENHAS("senhas", "Senhas seguras", List.of(
            "Use senhas longas e diferentes para cada aplicativo ou site.",
            "Evite datas de nascimento ou nomes fáceis de adivinhar.",
            "Nunca compartilhe suas senhas com ninguém, nem por telefone ou mensagem.",
            "Quando possível, ative a verificação em duas etapas para mais proteção."
    )),
    PRIVACIDADE("privacidade", "Privacidade", List.of(
            "Tenha cuidado ao compartilhar informações pessoais em redes sociais.",
            "Configure quem pode ver suas fotos e publicações.",
            "Desconfie de pedidos de dados pessoais por telefone, mensagem ou e-mail.",
            "Só compartilhe informações importantes com pessoas e instituições de confiança."
    ));

    private final String slug;
    private final String title;
    private final List<String> paragraphs;

    MaterialTopic(String slug, String title, List<String> paragraphs) {
        this.slug = slug;
        this.title = title;
        this.paragraphs = paragraphs;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public static Optional<MaterialTopic> fromSlug(String slug) {
        return Arrays.stream(values()).filter(topic -> topic.slug.equals(slug)).findFirst();
    }
}
