package model;

public class ProdutoModel {
    private int id;
    private int usuarioId;
    private String nome;
    private double preco;
    private UsuarioModel usuario; 
    
    
    public ProdutoModel() {}
    
    public ProdutoModel(int id, int usuarioId, String nome, double preco) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.preco = preco;
    }
    
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public double getPreco() {
        return preco;
    }
    
    public void setPreco(double preco) {
        this.preco = preco;
    }
    
    public UsuarioModel getUsuario() {
        return usuario;
    }
    
    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public String toString() {
        return "ProdutoModel{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                '}';
    }
}