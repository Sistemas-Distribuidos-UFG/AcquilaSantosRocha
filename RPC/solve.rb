class Solve
  attr_accessor :pType, :data

  def initialize pType, data
    @pType = pType
    @data = data
  end

  def run
    return send("q#{pType}")
  end

  def q1
    res = {}
    if data['cargo'] == 'operador'
      res = {'nome': data['nome'], 'salario': data['salario'].to_f*1.2}
    elsif data['cargo'] == 'programador'
      res = {'nome': data['nome'], 'salario': data['salario'].to_f*1.18}
    else
      res = {'nome': data['nome'], 'salario': data['salario'].to_f}
    end

    return res
  end

  def q2
    res = {}
    if (data['sexo'] == 'masculino' and data['idade'].to_i >= 18) or (data['sexo'] == 'feminino' and data['idade'].to_i >= 21)
      res = {'nome': data['nome'], 'maioridade': True}
    else
      res = {'nome': data['nome'], 'maioridade': False}
    end

    return res
  end

  def q3
    res = {}
    m = (data['n1'].to_f + data['n2'].to_f)/2.0
    if (m >= 7.0) or ((m > 3.0 and m < 7.0) and (m + data['n3'].to_f)/2.0 >= 5.0)
      res = {'aprovado': true}
    else
      res = {'aprovado': false}
    end

    return res
  end

  def q4
    res = {}
    if data['sexo'] == 'masculino'
      res = {'sexo': data['sexo'], 'peso': 72.7 * data['altura'].to_f - 58}
    else
      res = {'sexo': data['sexo'], 'peso': 62.1 * data['altura'].to_f - 44.7}
    end

    return res
  end

  def q5
    res = {}
    idade = data['idade'].to_i
    if idade >= 5 and idade <= 7
      res = {'idade': idade, 'categora': 'INFANTIL A'}
    elsif idade >= 8 and idade <= 10
      res = {'idade': idade, 'categora': 'INFANTIL B'}
    elsif idade >= 11 and idade <= 13
      res = {'idade': idade, 'categora': 'JUVENIL A'}
    elsif idade >= 14 and idade <= 17
      res = {'idade': idade, 'categora': 'JUVENIL B'}
    elsif idade >= 18
      res = {'idade': idade, 'categora': 'ADULTO'}
    else
      res = {'idade': idade, 'categora': 'inapto'}
    end

    return res
  end

  def q6
    res = {}
    nome = data['nome']
    nivel = data['nivel']
    salario = data['salario'].to_f
    dependentes = data['dependentes'].to_i

    if nivel == 'A'
      res = {'nome': data['nome'], 'liquido': dependentes == 0 ? salario*0.97 : salario*0.92}
    elsif nivel == 'B'
      res = {'nome': data['nome'], 'liquido': dependentes == 0 ? salario*0.95 : salario*0.90}
    elsif nivel == 'C'
      res = {'nome': data['nome'], 'liquido': dependentes == 0 ? salario*0.92 : salario*0.85}
    elsif nivel == 'D'
      res = {'nome': data['nome'], 'liquido': dependentes == 0 ? salario*0.90 : salario*0.83}
    else
      res = {'nome': data['nome'], 'liquido': salario}
    end

    return res
  end

  def q7
    res = {}
    idade = data['idade'].to_i
    tempo = data['tempo'].to_i

    if (idade >= 65 and tempo >= 30) or (idade >= 60 and tempo >= 25)
      res = {'idade': idade, 'tempo': tempo, 'aposenta': True}
    else
      res = {'idade': idade, 'tempo': tempo, 'aposenta': False}
    end

    return res
  end

  def q8
    res = {}
    saldo = data['saldo'].to_f

    if saldo >= 0 and saldo <= 200
      res = {'saldo': saldo, 'credito': 'Nenhum'}
    elsif saldo >= 201 and saldo <= 400
      res = {'saldo': saldo, 'credito': saldo*0.2}
    elsif saldo >= 401 and saldo <= 600
      res = {'saldo': saldo, 'credito': saldo*0.3}
    elsif saldo >= 601
      res = {'saldo': saldo, 'credito': saldo*0.4}
    end

    return res
  end


  def q9
    res = {}
    valor = data['valor'].to_i
    naipe = data['naipe'].to_i

    res = {'valor': valor, 'naipe': naipe}

    return res
  end
end

