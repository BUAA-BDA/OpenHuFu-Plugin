package com.hufudb.openhufu.mpc.aby.party;

import java.util.Arrays;
import java.util.List;

import com.hufudb.openhufu.mpc.aby.party.e_role;
import com.hufudb.openhufu.mpc.codec.OpenHuFuCodec;
import com.hufudb.openhufu.proto.OpenHuFuData.ColumnType;
import com.hufudb.openhufu.proto.OpenHuFuPlan.OperatorType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Aby4jParty {
  final int pid;
  final String address;
  final int port;
  final Party party;
  private static final Logger LOG = LoggerFactory.getLogger(Aby4jParty.class);

  public Aby4jParty(int partyId, String address, int port) {
    this.pid = partyId;
    this.address = address;
    this.port = port;
    this.party = new Party(partyId, address, port);
    LOG.info("aby: {}, {}, {}", partyId, address, port);
  }

  List<byte[]> greater(ColumnType type, e_role role, List<byte[]> inputs) {
    switch(type) {
      case BYTE:
      case SHORT:
      case INT:
        boolean result;
        int input = OpenHuFuCodec.decodeInt(inputs.get(0));
        LOG.info("input = {}, role = {}", input, role);
        if (role == e_role.SERVER) {
          result = party.GreaterI32(role, input, 0);
        }
        else {
          result = party.GreaterI32(role, 0, input);
        }
        LOG.info("result = {}", result);
        return Arrays.asList(OpenHuFuCodec.encodeBoolean(result));
      default:
        throw new UnsupportedOperationException("Unsupport type for aby");
    }
  }

  List<byte[]> multi_cmp(OperatorType op, ColumnType type, e_role role, Boolean shared, List<byte[]> inputs) {
    switch(type) {
      case BYTE:
      case SHORT:
      case INT:
        int v0 = OpenHuFuCodec.decodeInt(inputs.get(0));
        int v1 = OpenHuFuCodec.decodeInt(inputs.get(1));
        Integer result;
        if (op == OperatorType.MAX) {
          result = party.MaxI32(role, v0, v1, shared);
        }
        else {
          result = party.MinI32(role, v0, v1, shared);
        }
        LOG.info("aby_cmp: v0 = {}, v1 = {}, result = {}", Integer.toHexString(v0), Integer.toHexString(v1), Integer.toHexString(result));
        return Arrays.asList(OpenHuFuCodec.encodeInt(result));
      default:
        throw new UnsupportedOperationException("Unsupport type for aby");
    }
  }

  public List<byte[]> runProtocol(OperatorType op, ColumnType type, e_role role, Boolean shared, List<byte[]> inputs) {
    switch(op) {
      case GT:
        return greater(type, role, inputs);
      case MAX:
        return multi_cmp(op, type, role, shared, inputs);
      case MIN:
        return multi_cmp(op, type, role, shared, inputs);
      default:
        throw new UnsupportedOperationException("Unsupport operation for aby");
    }
  }
}
