package com.hufudb.openhufu.mpc.aby;

import java.util.List;

import com.hufudb.openhufu.mpc.ProtocolException;
import com.hufudb.openhufu.mpc.aby.party.Aby4jParty;
import com.hufudb.openhufu.mpc.aby.party.e_role;
import com.hufudb.openhufu.mpc.ProtocolExecutor;
import com.hufudb.openhufu.mpc.ProtocolType;
import com.hufudb.openhufu.proto.OpenHuFuData.ColumnType;
import com.hufudb.openhufu.proto.OpenHuFuPlan.OperatorType;
import com.hufudb.openhufu.proto.OpenHuFuService.OwnerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java Wrapper of ABY
 */
public class Aby implements ProtocolExecutor {
  OwnerInfo self;
  String address;
  int port;
  Aby4jParty abyParty;
  private static final Logger LOG = LoggerFactory.getLogger(Aby.class);

  public enum Role {
    SERVER, // ALICE
    CLIENT; // BOB
  }

  static {
    // todo: load for different os
    System.loadLibrary("abyparty4j");
  }

  @Override
  public int getOwnId() {
    return self.getId();
  }

  @Override
  public ProtocolType getProtocolType() {
    return ProtocolType.ABY;
  }

  @Override
  public int getProtocolTypeId() {
    return ProtocolType.ABY.getId();
  }

  public Aby(int id, String address, int port) {
    this.self = OwnerInfo.newBuilder().setId(id).setEndpoint(String.format("%s:%d", address, port)).build();
    this.address = address;
    this.port = port;
    this.abyParty = new Aby4jParty(id, address, port);
  }

  public Aby(OwnerInfo self) {
    this.self = self;
    String[] parts = self.getEndpoint().split(":", 2);
    this.address = parts[0];
    this.port = Integer.valueOf(parts[1]);
    this.abyParty = new Aby4jParty(self.getId(), address, port);
  }

  @Override
  public Object run(long taskId, List<Integer> parties, Object... args) throws ProtocolException {
    List<byte[]> inputData = (List<byte[]>) args[0];
    OperatorType opType = (OperatorType) args[1];
    ColumnType type = (ColumnType) args[2];
    String address = (String) args[3];
    int port = (int) args[4];
    boolean shared = (boolean) args[5];
    e_role role = parties.get(0) == 0? e_role.SERVER : e_role.CLIENT;
    Object ret = abyParty.runProtocol(opType, type, role, shared, inputData);
    return ret; 
  }
}
